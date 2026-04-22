package com.vitordev.ticket.orders.messaging;


import com.vitordev.ticket.orders.model.dto.EventMessage;
import com.vitordev.ticket.orders.model.dto.PaymentApprovedMessage;
import com.vitordev.ticket.orders.model.dto.PaymentFailedMessage;
import com.vitordev.ticket.orders.service.OrderService;
import com.vitordev.ticket.orders.service.RedisService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    @Autowired private RedisService redisService;
    @Autowired private OrderService orderService;

    @RabbitListener(queues = "event.created.queue")
    public void eventCreate(EventMessage eventMessage){
        redisService.stockInitializer(eventMessage);
    }

    @RabbitListener(queues = "payment.approved.queue")
    public void paymentApproved(PaymentApprovedMessage paymentApprovedMessage){
        orderService.approvedPayment(paymentApprovedMessage);
    }

    @RabbitListener(queues = "payment.failed.queue")
    public void paymentFailed(PaymentFailedMessage paymentFailedMessage){
        orderService.failedPayment(paymentFailedMessage);
    }
}
