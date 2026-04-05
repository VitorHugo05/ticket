package com.vitordev.ticket.orders.repository;

import com.vitordev.ticket.orders.model.OrderEntity;
import com.vitordev.ticket.orders.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatusAndExpiresAtBefore(OrderStatus orderStatus, LocalDateTime now);
}
