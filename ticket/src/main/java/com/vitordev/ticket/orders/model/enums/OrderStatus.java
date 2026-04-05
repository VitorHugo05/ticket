package com.vitordev.ticket.orders.model.enums;

public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    FAILED("Failed"),
    CANCELLED("Cancelled");

    private String status;

    OrderStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
