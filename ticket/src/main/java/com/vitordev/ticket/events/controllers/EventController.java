package com.vitordev.ticket.events.controllers;

import com.vitordev.ticket.events.model.dto.EventRequestDto;
import com.vitordev.ticket.events.model.dto.EventResponseDto;
import com.vitordev.ticket.events.model.dto.EventUpdateRequestDto;
import com.vitordev.ticket.events.model.entities.EventEntity;
import com.vitordev.ticket.events.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired private EventService eventService;

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody EventRequestDto eventRequestDto){
        EventEntity eventEntity = eventService.insert(eventRequestDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eventEntity.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> findAll(){
        List<EventEntity> eventEntityList = eventService.findAll();
        List<EventResponseDto> eventResponseDtoList = eventEntityList.stream()
                .map(
                        eventEntity -> new EventResponseDto(
                                eventEntity.getId(),
                                eventEntity.getName(),
                                eventEntity.getStartTime(),
                                eventEntity.getEndTime(),
                                eventEntity.getDescription(),
                                eventEntity.getCapacity(),
                                eventEntity.getSold()
                        )
                )
                .toList();
        return ResponseEntity.ok().body(eventResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> findById(@PathVariable Long id){
        EventEntity eventEntity = eventService.findById(id);
        EventResponseDto eventResponseDto = new EventResponseDto(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getStartTime(),
                eventEntity.getEndTime(),
                eventEntity.getDescription(),
                eventEntity.getCapacity(),
                eventEntity.getSold()
        );
        return ResponseEntity.ok().body(eventResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        eventService.update(id, eventUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
