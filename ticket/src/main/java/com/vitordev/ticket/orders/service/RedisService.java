package com.vitordev.ticket.orders.service;

import com.vitordev.ticket.orders.model.dto.EventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisService {

    @Autowired private Jedis jedis;

    public void stockInitializer(EventMessage eventMessage) {
        String key = "event:" + eventMessage.getId() + ":available";
        jedis.set(key, String.valueOf(eventMessage.getCapacity()));
    }
}
