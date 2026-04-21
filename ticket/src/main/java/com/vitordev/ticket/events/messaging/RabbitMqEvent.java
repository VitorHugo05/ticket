package com.vitordev.ticket.events.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqEvent {

    public static final String EVENT_CREATED_EXCHANGE = "event.created.exchange";
    public static final String EVENT_CREATED_QUEUE = "event.created.queue";
    public static final String EVENT_CREATED_ROUTING_KEY = "event.created";

    public static final String GET_EVENT_EXCHANGE = "event.exchange";
    public static final String GET_EVENT_QUEUE = "event.queue";
    public static final String GET_EVENT_ROUTING_KEY = "event";

    @Bean
    public DirectExchange getEventExchange(){
        return new DirectExchange(GET_EVENT_EXCHANGE);
    }

    @Bean
    public Queue getEventQueue(){
        return QueueBuilder.durable(GET_EVENT_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "event.dlq")
                .build();
    }

    @Bean
    public Binding getEventBinding(){
        return BindingBuilder
                .bind(getEventQueue())
                .to(getEventExchange())
                .with(GET_EVENT_ROUTING_KEY);
    }

    @Bean
    public DirectExchange eventCreatedExchange() {
        return new DirectExchange(EVENT_CREATED_EXCHANGE);
    }

    @Bean
    public Queue eventCreatedQueue() {
        return QueueBuilder.durable(EVENT_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "event.created.dlq")
                .build();
    }

    @Bean
    public Binding eventCreatedBinding() {
        return BindingBuilder
                .bind(eventCreatedQueue())
                .to(eventCreatedExchange())
                .with(EVENT_CREATED_ROUTING_KEY);
    }
}
