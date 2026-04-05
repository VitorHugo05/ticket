package com.vitordev.ticket.orders.model.dto;

public class EventMessage {

    private Long id;
    private Integer capacity;
    private Integer sold;

    public EventMessage() { }

    public EventMessage(Long id, Integer capacity, Integer sold) {
        this.id = id;
        this.capacity = capacity;
        this.sold = sold;
    }

    public Long getId() {
        return id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getSold() {
        return sold;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }
}
