package com.vitordev.ticket.orders.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderCreatedMessage implements Serializable {
    private Long id;

    private Long userId;
    private Long eventId;
    private Integer quantity;

    private LocalDateTime expiresAt;

    public OrderCreatedMessage(Long id, Long userId, Long eventId, Integer quantity, LocalDateTime expiresAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.quantity = quantity;
        this.expiresAt = expiresAt;
    }

    public OrderCreatedMessage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
