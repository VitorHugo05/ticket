package com.vitordev.ticket.orders.controllers;

import com.vitordev.ticket.orders.model.OrderEntity;
import com.vitordev.ticket.orders.model.dto.OrderRequestDto;
import com.vitordev.ticket.orders.model.dto.OrderResponseDto;
import com.vitordev.ticket.orders.model.dto.OrderUpdateRequestDto;
import com.vitordev.ticket.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired private OrderService orderService;

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserve(@RequestBody OrderRequestDto orderRequestDto, @RequestHeader("Idempotency-Key") String idempotencyKey){
        OrderEntity orderEntity = orderService.createOrder(idempotencyKey, orderRequestDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderEntity.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable Long id){
        OrderResponseDto orderResponseDto = orderService.findById(id);
        return ResponseEntity.ok().body(orderResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findAll() {
        List<OrderResponseDto> orderResponseDtoList = orderService.findAll();
        return ResponseEntity.ok().body(orderResponseDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody OrderUpdateRequestDto orderUpdateRequestDto){
        orderService.update(id, orderUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
