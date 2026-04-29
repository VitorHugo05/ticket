package com.vitordev.ticket.events.model.dto;

import java.io.Serializable;

public class OrderConfirmedMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long eventId;
    private Integer quantity;

    public OrderConfirmedMessage(Integer quantity, Long eventId) {
        this.quantity = quantity;
        this.eventId = eventId;
    }

    public OrderConfirmedMessage() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
