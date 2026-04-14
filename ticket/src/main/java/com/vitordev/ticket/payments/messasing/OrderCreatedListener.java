package com.vitordev.ticket.payments.messasing;

import com.vitordev.ticket.payments.model.dto.OrderCreatedMessage;
import com.vitordev.ticket.payments.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    @Autowired private PaymentService paymentService;

    @RabbitListener(queues = "order.created.queue")
    public void orderListener(OrderCreatedMessage orderCreatedMessage){
        paymentService.createPayment(orderCreatedMessage);
    }
}
