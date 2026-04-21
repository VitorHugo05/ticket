package com.vitordev.ticket.events.messaging;

import com.vitordev.ticket.events.model.dto.EventDto;
import com.vitordev.ticket.events.model.entities.EventEntity;
import com.vitordev.ticket.events.services.EventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventListener {

    @Autowired private EventService eventService;

    @RabbitListener(queues = "event.queue")
    public EventDto getEventProcess(Long message){
        EventEntity eventEntity = eventService.findById(message);
        return new EventDto(eventEntity.getId(), eventEntity.getTicketPrice());
    }
}
