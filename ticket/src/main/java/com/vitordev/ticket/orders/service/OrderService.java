package com.vitordev.ticket.orders.service;

import com.vitordev.ticket.events.messaging.RabbitMqEvent;
import com.vitordev.ticket.orders.messaging.RabbitMqOrder;
import com.vitordev.ticket.orders.model.IdempotencyKeyEntity;
import com.vitordev.ticket.orders.model.OrderEntity;
import com.vitordev.ticket.orders.model.dto.*;
import com.vitordev.ticket.orders.model.enums.OrderStatus;
import com.vitordev.ticket.orders.repository.IdempotencyRepository;
import com.vitordev.ticket.orders.repository.OrderRepository;
import com.vitordev.ticket.shared.exceptions.BadRequestArgumentException;
import com.vitordev.ticket.shared.exceptions.DuplicateRequestException;
import com.vitordev.ticket.shared.exceptions.InsufficientResourceException;
import com.vitordev.ticket.shared.exceptions.ObjectNotFoundException;
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

    public EventDto getEvent(Long id) {
        Object response = rabbitTemplate.convertSendAndReceive(
                "event.exchange",
                "event",
                id
        );

        if (response == null) {
            throw new ObjectNotFoundException("Event not found");
        }

        String json = objectMapper.writeValueAsString(response);
        return objectMapper.readValue(json, EventDto.class);
    }

    @Transactional
    public OrderEntity createOrder(String key, OrderRequestDto request) {

        EventDto eventDto = getEvent(request.getEventId());

        if (idempotencyRepository.findByIdempotencyKey(key).isPresent()) {
            throw new DuplicateRequestException("Request already processed with idempotency key");
        }

        String redisKey = "event:" + request.getEventId() + ":available";

        if (!jedis.exists(redisKey)) {
            throw new ObjectNotFoundException("Event not found or not available");
        }

        Long remaining = jedis.decrBy(redisKey, request.getQuantity());

        if (remaining < 0) {
            jedis.incrBy(redisKey, request.getQuantity());
            throw new InsufficientResourceException("No stock available");
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

        rabbitTemplate.convertAndSend(
                RabbitMqOrder.ORDER_EXCHANGE,
                RabbitMqOrder.ORDER_CREATED_ROUTING_KEY,
                new OrderCreatedMessage(savedOrder.getId(), savedOrder.getEventId(),
                        savedOrder.getUserId(), savedOrder.getQuantity(), savedOrder.getPrice(),
                        savedOrder.getExpiresAt()));

        return savedOrder;
    }

    public OrderResponseDto findById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Order not found or not available"));
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
                .orElseThrow(() -> new ObjectNotFoundException("Order not found"));

        if(order.getStatus() != OrderStatus.PENDING){
            throw new BadRequestArgumentException("Order already processed.");
        }

        String redisKey = "event:" + order.getEventId() + ":available";

        int oldQuantity = order.getQuantity();

        int diff = request.getQuantity() - oldQuantity;

        if (diff < 0) {
            jedis.incrBy(redisKey, Math.abs(diff));
        }

        else if (diff > 0) {
            Long remaining = jedis.decrBy(redisKey, diff);
            if (remaining < 0) {
                jedis.incrBy(redisKey, diff);
                throw new InsufficientResourceException("No stock available");
            }
        }

        order.setQuantity(request.getQuantity());
        order.setUpdatedAt(LocalDateTime.now());

        rabbitTemplate.convertAndSend(
                RabbitMqOrder.ORDER_EXCHANGE,
                RabbitMqOrder.ORDER_UPDATE_ROUTING_KEY,
                new OrderUpdatedMessage(order.getId(), request.getQuantity(), order.getPrice())
        );

        orderRepository.save(order);
    }

    public void delete(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order not found"));
        String redisKey = "event:" + order.getEventId() + ":available";
        jedis.incrBy(redisKey, order.getQuantity());
        orderRepository.deleteById(id);
    }

    public void approvedPayment(PaymentApprovedMessage paymentApprovedMessage) {
        OrderEntity orderEntity = orderRepository.findById(paymentApprovedMessage.getOrderId())
                .orElseThrow(() -> new ObjectNotFoundException("Order not found or not available"));

        orderEntity.setStatus(OrderStatus.CONFIRMED);
        orderEntity.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(orderEntity);
    }

    public void failedPayment(PaymentFailedMessage paymentFailedMessage) {
        OrderEntity orderEntity = orderRepository.findById(paymentFailedMessage.getOrderId())
                .orElseThrow(() -> new ObjectNotFoundException("Order not found or not available"));

        String redisKey = "event:" + orderEntity.getEventId() + ":available";

        jedis.incrBy(redisKey, orderEntity.getQuantity());

        orderEntity.setStatus(OrderStatus.FAILED);
        orderEntity.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(orderEntity);
    }
}
