package com.vitordev.ticket.payments.service;

import com.vitordev.ticket.payments.model.PaymentEntity;
import com.vitordev.ticket.payments.model.dto.OrderCreatedMessage;
import com.vitordev.ticket.payments.model.enums.PaymentStatus;
import com.vitordev.ticket.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;

    public void createPayment(OrderCreatedMessage orderCreatedMessage){
        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setAmount(orderCreatedMessage.getPrice() * orderCreatedMessage.getQuantity());
        paymentEntity.setCreatedAt(LocalDateTime.now());
        paymentEntity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        paymentEntity.setUpdatedAt(LocalDateTime.now());
        paymentEntity.setOrderId(orderCreatedMessage.getId());
        paymentEntity.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(paymentEntity);
    }
}
