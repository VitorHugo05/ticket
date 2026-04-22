package com.vitordev.ticket.payments.repository;

import com.vitordev.ticket.orders.model.OrderEntity;
import com.vitordev.ticket.orders.model.enums.OrderStatus;
import com.vitordev.ticket.payments.model.PaymentEntity;
import com.vitordev.ticket.payments.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByOrderId(Long orderId);

    List<PaymentEntity> findByStatusAndExpiresAtBefore(PaymentStatus paymentStatus, LocalDateTime now);
}
