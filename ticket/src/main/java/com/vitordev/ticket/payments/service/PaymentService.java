package com.vitordev.ticket.payments.service;

import com.vitordev.ticket.payments.model.PaymentEntity;
import com.vitordev.ticket.payments.model.dto.*;
import com.vitordev.ticket.payments.model.enums.PaymentMethods;
import com.vitordev.ticket.payments.model.enums.PaymentStatus;
import com.vitordev.ticket.payments.repository.PaymentRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private RabbitTemplate rabbitTemplate;

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

    public PaymentResponseDto pay(PaymentDto paymentDto, Long orderId) {
        PaymentEntity paymentEntity = paymentRepository.findByOrderId(orderId);
        paymentEntity.setMethod(PaymentMethods.valueOf(paymentDto.getMethods()));
        Random random = new Random();
        boolean randomBoolean = random.nextBoolean();

        if(randomBoolean){
            paymentEntity.setStatus(PaymentStatus.APPROVED);
            paymentRepository.save(paymentEntity);
            rabbitTemplate.convertAndSend(
                    "payemnt.exchange",
                    "payment.approved",
                    new PaymentApprovedMessage(paymentEntity.getId(), paymentEntity.getOrderId(), paymentEntity.getAmount(), LocalDateTime.now())
            );
            return new PaymentResponseDto(System.currentTimeMillis(), PaymentStatus.APPROVED, "Approved");
        }

        paymentEntity.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(paymentEntity);
        rabbitTemplate.convertAndSend(
                "payemnt.exchange",
                "payment.failed",
                new PaymentFailedMessage(paymentEntity.getId(), paymentEntity.getOrderId(), paymentEntity.getAmount(), LocalDateTime.now())
        );
        return new PaymentResponseDto(System.currentTimeMillis(), PaymentStatus.FAILED, "Failed");
    }
}
