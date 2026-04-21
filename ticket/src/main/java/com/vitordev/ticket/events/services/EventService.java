package com.vitordev.ticket.events.services;

import com.vitordev.ticket.events.messaging.RabbitMqEvent;
import com.vitordev.ticket.events.model.dto.EventMessage;
import com.vitordev.ticket.events.model.dto.EventRequestDto;
import com.vitordev.ticket.events.model.dto.EventUpdateRequestDto;
import com.vitordev.ticket.events.model.entities.EventEntity;
import com.vitordev.ticket.events.repository.EventRepository;
import com.vitordev.ticket.shared.exceptions.BadRequestArgumentException;
import com.vitordev.ticket.shared.exceptions.ObjectNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired private EventRepository eventRepository;
    @Autowired private RabbitTemplate rabbitTemplate;

    @Transactional
    public EventEntity insert(EventRequestDto eventRequestDto) {

        if(eventRequestDto.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestArgumentException("Start time cannot be in the past");
        }

        if(eventRequestDto.getEndTime().isBefore(eventRequestDto.getStartTime())) {
            throw new BadRequestArgumentException("End time must be after start time");
        }

        EventEntity eventEntity = new EventEntity();

        eventEntity.setName(eventRequestDto.getName());
        eventEntity.setDescription(eventRequestDto.getDescription());
        eventEntity.setStartTime(eventRequestDto.getStartTime());
        eventEntity.setEndTime(eventRequestDto.getEndTime());
        eventEntity.setCapacity(eventRequestDto.getTickets());
        eventEntity.setTicketPrice(eventRequestDto.getTicketPrice());

        eventRepository.save(eventEntity);

        publishEventCreated(eventEntity);

        return eventEntity;
    }

    private void publishEventCreated(EventEntity event) {
        EventMessage message = new EventMessage(
                event.getId(),
                event.getCapacity(),
                event.getSold()
        );

        rabbitTemplate.convertAndSend(
                RabbitMqEvent.EVENT_CREATED_EXCHANGE,
                RabbitMqEvent.EVENT_CREATED_ROUTING_KEY,
                message
        );
    }

    public List<EventEntity> findAll() {
        return eventRepository.findAll();
    }

    public EventEntity findById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Object not found"));
    }

    public void update(Long id, EventUpdateRequestDto eventUpdateRequestDto) {
        EventEntity eventEntity = findById(id);

        if(eventUpdateRequestDto.getDescription() != null) eventEntity.setDescription(eventUpdateRequestDto.getDescription());
        if(eventUpdateRequestDto.getName() != null) eventEntity.setName(eventUpdateRequestDto.getName());
        if(eventUpdateRequestDto.getStartTime() != null) eventEntity.setStartTime(eventUpdateRequestDto.getStartTime());
        if(eventUpdateRequestDto.getEndTime() != null) eventEntity.setEndTime(eventUpdateRequestDto.getEndTime());

        eventRepository.save(eventEntity);
    }

    public void delete(Long id) {
        findById(id);
        eventRepository.deleteById(id);
    }
}
