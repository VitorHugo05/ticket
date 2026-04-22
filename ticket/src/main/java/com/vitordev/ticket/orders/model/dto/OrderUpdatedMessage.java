package com.vitordev.ticket.orders.model.dto;

public class OrderUpdatedMessage {
    private Long orderId;
    private Integer quantity;
    private Double ticketPrice;

    public OrderUpdatedMessage(Long orderId, Integer quantity, Double ticketPrice) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.ticketPrice = ticketPrice;
    }

    public OrderUpdatedMessage() {
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
