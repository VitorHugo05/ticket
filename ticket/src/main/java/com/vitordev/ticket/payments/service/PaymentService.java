package com.vitordev.ticket.payments.service;

import com.vitordev.ticket.payments.messasing.RabbitMqPayment;
import com.vitordev.ticket.payments.model.PaymentEntity;
import com.vitordev.ticket.payments.model.dto.*;
import com.vitordev.ticket.payments.model.enums.PaymentMethods;
import com.vitordev.ticket.payments.model.enums.PaymentStatus;
import com.vitordev.ticket.payments.repository.PaymentRepository;
import com.vitordev.ticket.shared.exceptions.BadRequestArgumentException;
import com.vitordev.ticket.shared.exceptions.ObjectNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void expirePendingPayments() {
        List<PaymentEntity> expired = paymentRepository.findByStatusAndExpiresAtBefore(PaymentStatus.PENDING, LocalDateTime.now());
        for (PaymentEntity payment : expired) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            rabbitTemplate.convertAndSend(
                    RabbitMqPayment.PAYMENT_EXCHANGE,
                    RabbitMqPayment.PAYMENT_FAILED_ROUTING_KEY,
                    new PaymentFailedMessage(payment.getId(), payment.getOrderId(), payment.getAmount(), LocalDateTime.now())
            );
            paymentRepository.save(payment);
        }
    }

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
        PaymentEntity paymentEntity = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Payment not found"));

        if(paymentEntity.getStatus() != PaymentStatus.PENDING){
            throw new BadRequestArgumentException("Payment order already processed.");
        }

        validatePaymentMethod(paymentDto);
        paymentEntity.setMethod(PaymentMethods.valueOf(paymentDto.getMethods()));
        Random random = new Random();
        boolean randomBoolean = random.nextBoolean();

        if(randomBoolean){
            paymentEntity.setStatus(PaymentStatus.APPROVED);
            paymentRepository.save(paymentEntity);
            rabbitTemplate.convertAndSend(
                    RabbitMqPayment.PAYMENT_EXCHANGE,
                    RabbitMqPayment.PAYMENT_APPROVED_ROUTING_KEY,
                    new PaymentFailedMessage(paymentEntity.getId(), paymentEntity.getOrderId(), paymentEntity.getAmount(), LocalDateTime.now())
            );
            return new PaymentResponseDto(System.currentTimeMillis(), PaymentStatus.APPROVED, "Approved");
        }

        paymentEntity.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(paymentEntity);
        rabbitTemplate.convertAndSend(
                RabbitMqPayment.PAYMENT_EXCHANGE,
                RabbitMqPayment.PAYMENT_FAILED_ROUTING_KEY,
                new PaymentFailedMessage(paymentEntity.getId(), paymentEntity.getOrderId(), paymentEntity.getAmount(), LocalDateTime.now())
        );
        return new PaymentResponseDto(System.currentTimeMillis(), PaymentStatus.FAILED, "Failed");
    }

    public void updatePayment(OrderUpdatedMessage orderUpdatedMessage){
        PaymentEntity paymentEntity = paymentRepository.findByOrderId(orderUpdatedMessage.getOrderId())
                .orElseThrow(() -> new ObjectNotFoundException("Payment not found"));;

        if (paymentEntity.getStatus() != PaymentStatus.PENDING) {
            throw new BadRequestArgumentException("Payment already processed, cannot update.");
        }

        paymentEntity.setAmount(orderUpdatedMessage.getTicketPrice() * orderUpdatedMessage.getQuantity());
        paymentEntity.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(paymentEntity);
    }

    public void validatePaymentMethod(PaymentDto paymentDto) {
        try {
            PaymentMethods.valueOf(paymentDto.getMethods().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestArgumentException(paymentDto.getMethods());
        }
    }
}
