package com.vitordev.ticket.payments.controllers;

import com.vitordev.ticket.payments.model.dto.PaymentDto;
import com.vitordev.ticket.payments.model.dto.PaymentResponseDto;
import com.vitordev.ticket.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired private PaymentService paymentService;

    @PostMapping("/pay/{orderId}")
    public PaymentResponseDto payMethod(@RequestBody PaymentDto paymentDto, @PathVariable Long orderId){
        return paymentService.pay(paymentDto, orderId);
    }
}
