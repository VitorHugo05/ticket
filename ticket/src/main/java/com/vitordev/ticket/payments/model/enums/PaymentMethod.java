package com.vitordev.ticket.payments.model.enums;

public enum PaymentMethod {
    CREDIT("Credit"),
    DEBIT("Debit"),
    PIX("Pix");

    private String method;

    PaymentMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
