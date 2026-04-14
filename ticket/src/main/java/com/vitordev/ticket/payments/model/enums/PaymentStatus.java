package com.vitordev.ticket.payments.model.enums;

public enum PaymentStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    FAILED("Failed");

    private String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
