package com.vitordev.ticket.events.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EVENT_CREATED_EXCHANGE = "event.created.exchange";
    public static final String EVENT_CREATED_QUEUE = "event.created.queue";
    public static final String EVENT_CREATED_ROUTING_KEY = "event.created";

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

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
