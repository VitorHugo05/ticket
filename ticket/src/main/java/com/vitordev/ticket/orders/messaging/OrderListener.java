package com.vitordev.ticket.orders.messaging;


import com.vitordev.ticket.orders.model.dto.EventMessage;
import com.vitordev.ticket.orders.service.RedisService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    @Autowired private RedisService redisService;

    @RabbitListener(queues = "event.created.queue")
    public void eventCreate(EventMessage eventMessage){
        redisService.stockInitializer(eventMessage);
    }
}
