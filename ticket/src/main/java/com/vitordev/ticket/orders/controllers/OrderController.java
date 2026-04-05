package com.vitordev.ticket.orders.controllers;

import com.vitordev.ticket.orders.model.OrderEntity;
import com.vitordev.ticket.orders.model.dto.OrderRequestDto;
import com.vitordev.ticket.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
}
