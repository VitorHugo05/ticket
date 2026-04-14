package com.vitordev.ticket.payments.messasing;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqPayment {
    public static final String PAYMENT_EXCHANGE = "payemnt.exchange";

    public static final String PAYMENT_APPROVED_QUEUE = "payment.approved.queue";
    public static final String PAYMENT_APPROVED_ROUTING_KEY = "payment.approved";

    public static final String PAYMENT_FAILED_QUEUE = "payment.failed.queue";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.failed";

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentApprovedQueue() {
        return QueueBuilder.durable(PAYMENT_APPROVED_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "order.created.dlq")
                .build();
    }

    @Bean
    public Binding paymentApprovedBinding() {
        return BindingBuilder
                .bind(paymentApprovedQueue())
                .to(paymentExchange())
                .with(PAYMENT_APPROVED_ROUTING_KEY);
    }

    @Bean
    public Queue paymentFailedQueue() {
        return QueueBuilder.durable(PAYMENT_FAILED_QUEUE)
                .withArgument("x-dead-letter-exchange", "dlx.exchange")
                .withArgument("x-dead-letter-routing-key", "order.created.dlq")
                .build();
    }

    @Bean
    public Binding paymentFailedBinding(){
        return BindingBuilder
                .bind(paymentFailedQueue())
                .to(paymentExchange())
                .with(PAYMENT_FAILED_ROUTING_KEY);
    }
}
