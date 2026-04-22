package com.vitordev.ticket.payments.model.enums;

public enum PaymentMethods {
    DEBIT("Debit"),
    CREDIT("Credit"),
    PIX("Pix");

    private String methods;

    PaymentMethods(String methods) {
        this.methods = methods;
    }

    public String getMethods() {
        return methods;
    }
}
