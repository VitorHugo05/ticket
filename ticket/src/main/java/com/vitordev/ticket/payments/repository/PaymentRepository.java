package com.vitordev.ticket.payments.repository;

import com.vitordev.ticket.payments.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    PaymentEntity findByOrderId(Long orderId);
}
