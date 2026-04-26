package com.vitordev.ticket.orders.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {
    @Bean
    public Jedis jedis(
        @Value("${redis.host:localhost}") String host,
        @Value("${redis.port:6379}") int port
    ) {
        return new Jedis(host, port);
    }
}
