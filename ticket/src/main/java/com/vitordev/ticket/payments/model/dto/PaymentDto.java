package com.vitordev.ticket.payments.model.dto;

public class PaymentDto {
    private String methods;

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public PaymentDto(String methods) {
        this.methods = methods;
    }
}
