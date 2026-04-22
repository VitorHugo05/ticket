package com.vitordev.ticket.orders.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqOrder {
    public static final String ORDER_EXCHANGE = "order.exchange";

    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    public static final String ORDER_UPDATE_QUEUE = "order.update.queue";
    public static final String ORDER_UPDATE_ROUTING_KEY = "order.update";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderUpdateQueue() {
        return QueueBuilder.durable(ORDER_UPDATE_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "order.update.dlq")
                .build();
    }

    @Bean
    public Binding orderUpdateBinding() {
        return BindingBuilder
                .bind(orderUpdateQueue())
                .to(orderExchange())
                .with(ORDER_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return QueueBuilder.durable(ORDER_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "order.created.dlq")
                .build();
    }

    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder
                .bind(orderCreatedQueue())
                .to(orderExchange())
                .with(ORDER_CREATED_ROUTING_KEY);
    }
}
