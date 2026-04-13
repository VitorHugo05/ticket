package com.vitordev.ticket.orders.model.dto;

import com.vitordev.ticket.orders.model.enums.OrderStatus;

import java.time.LocalDateTime;

public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Long eventId;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private OrderStatus status;

    public OrderResponseDto() {
    }

    public OrderResponseDto(Long id, OrderStatus status, LocalDateTime expiresAt, LocalDateTime updatedAt, LocalDateTime createdAt, Integer quantity, Long eventId, Long userId) {
        this.id = id;
        this.status = status;
        this.expiresAt = expiresAt;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.quantity = quantity;
        this.eventId = eventId;
        this.userId = userId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
