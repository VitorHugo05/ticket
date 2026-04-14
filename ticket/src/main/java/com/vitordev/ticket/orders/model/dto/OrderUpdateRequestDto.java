package com.vitordev.ticket.orders.model.dto;

public class OrderUpdateRequestDto {

    private Integer quantity;

    public OrderUpdateRequestDto() {
    }

    public OrderUpdateRequestDto(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
