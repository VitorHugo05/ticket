package com.vitordev.ticket.orders.service;

import com.vitordev.ticket.orders.model.IdempotencyKeyEntity;
import com.vitordev.ticket.orders.model.OrderEntity;
import com.vitordev.ticket.orders.model.dto.*;
import com.vitordev.ticket.orders.model.enums.OrderStatus;
import com.vitordev.ticket.orders.repository.IdempotencyRepository;
import com.vitordev.ticket.orders.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired private IdempotencyRepository idempotencyRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RabbitTemplate rabbitTemplate;

    @Autowired private Jedis jedis;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void expirePendingOrders() {
        List<OrderEntity> expired = orderRepository.findByStatusAndExpiresAtBefore(OrderStatus.PENDING, LocalDateTime.now());
        for (OrderEntity order : expired) {
            String redisKey = "event:" + order.getEventId() + ":available";
            jedis.incrBy(redisKey, order.getQuantity());
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }
    }

    public EventDto getEvent(Long id){
        Object response = rabbitTemplate.convertSendAndReceive(
                "event.exchange",
                "event",
                id
        );

        String json = objectMapper.writeValueAsString(response);

        EventDto eventDto = objectMapper.readValue(json, EventDto.class);
        System.out.println(eventDto.toString());
        return eventDto;
    }

    @Transactional
    public OrderEntity createOrder(String key, OrderRequestDto request) {

        EventDto eventDto = getEvent(request.getEventId());

        if (idempotencyRepository.findByIdempotencyKey(key).isPresent()) {
            throw new RuntimeException("Request already processed with this idempotency key");
        }

        String redisKey = "event:" + request.getEventId() + ":available";

        if (!jedis.exists(redisKey)) {
            throw new RuntimeException("Event not found or not available");
        }

        Long remaining = jedis.decrBy(redisKey, request.getQuantity());

        if (remaining < 0) {
            jedis.incrBy(redisKey, request.getQuantity());
            throw new RuntimeException("No stock available");
        }

        OrderEntity newOrder = new OrderEntity(
                request.getUserId(),
                request.getEventId(),
                eventDto.getTicketPrice(),
                request.getQuantity(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(20),
                OrderStatus.PENDING
        );

        OrderEntity savedOrder = orderRepository.save(newOrder);

        String responseJson = objectMapper.writeValueAsString(savedOrder);


        IdempotencyKeyEntity newKey = new IdempotencyKeyEntity();
        newKey.setIdempotencyKey(key);
        newKey.setStatusCode(201);
        newKey.setResponseBody(responseJson);

        idempotencyRepository.save(newKey);

        rabbitTemplate.convertAndSend("order.created.exchange", "order.created",
                new OrderCreatedMessage(savedOrder.getId(), savedOrder.getEventId(),
                        savedOrder.getUserId(), savedOrder.getQuantity(), savedOrder.getPrice(),
                        savedOrder.getExpiresAt()));

        return savedOrder;
    }


    public OrderResponseDto findById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found or not available"));
        return toDto(orderEntity);
    }

    public List<OrderResponseDto> findAll() {
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        List<OrderResponseDto> orderResponseDtoList = orderEntityList.stream()
                .map(this::toDto)
                .toList();
        return orderResponseDtoList;
    }

    private OrderResponseDto toDto(OrderEntity orderEntity){
        OrderResponseDto orderResponseDto = new OrderResponseDto();

        orderResponseDto.setCreatedAt(orderEntity.getCreatedAt());
        orderResponseDto.setQuantity(orderEntity.getQuantity());
        orderResponseDto.setPrice(orderEntity.getPrice());
        orderResponseDto.setExpiresAt(orderEntity.getExpiresAt());
        orderResponseDto.setUpdatedAt(orderEntity.getUpdatedAt());
        orderResponseDto.setStatus(orderEntity.getStatus());
        orderResponseDto.setEventId(orderEntity.getEventId());
        orderResponseDto.setUserId(orderEntity.getUserId());
        orderResponseDto.setId(orderEntity.getId());

        return orderResponseDto;
    }

    public void update(Long id, OrderUpdateRequestDto request) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String redisKey = "event:" + order.getEventId() + ":available";

        int oldQuantity = order.getQuantity();
        int newQuantity = request.getQuantity();

        int diff = newQuantity - oldQuantity;

        if (diff < 0) {
            jedis.incrBy(redisKey, Math.abs(diff));
        }

        else if (diff > 0) {
            Long remaining = jedis.decrBy(redisKey, diff);
            if (remaining < 0) {
                jedis.incrBy(redisKey, diff);
                throw new RuntimeException("No stock available");
            }
        }
        order.setQuantity(newQuantity);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    public void delete(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        String redisKey = "event:" + order.getEventId() + ":available";
        jedis.incrBy(redisKey, order.getQuantity());
        orderRepository.deleteById(id);
    }
}
