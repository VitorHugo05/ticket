package com.vitordev.ticket.orders.service;

import com.vitordev.ticket.orders.model.IdempotencyKeyEntity;
import com.vitordev.ticket.orders.model.OrderEntity;
import com.vitordev.ticket.orders.model.dto.OrderMessage;
import com.vitordev.ticket.orders.model.dto.OrderRequestDto;
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

    @Transactional
    public OrderEntity createOrder(String key, OrderRequestDto request) {

        if (idempotencyRepository.findByIdempotencyKey(key).isPresent()) {
            throw new RuntimeException("Request already processed with this idempotency key");
        }

        String redisKey = "event:" + request.getEventId() + ":available";
        Long remaining = jedis.decrBy(redisKey, request.getQuantity());

        if (!jedis.exists(redisKey)) {
            throw new RuntimeException("Event not found or not available");
        }

        if (remaining < 0) {
            jedis.incrBy(redisKey, request.getQuantity());
            throw new RuntimeException("No stock available");
        }

        OrderEntity newOrder = new OrderEntity(
                request.getUserId(),
                request.getEventId(),
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
                new OrderMessage(savedOrder.getId(), savedOrder.getEventId(),
                        savedOrder.getUserId(), savedOrder.getQuantity(),
                        savedOrder.getExpiresAt()));

        return savedOrder;
    }
}
