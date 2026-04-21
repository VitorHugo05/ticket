package com.vitordev.ticket.orders.model.dto;

public class EventDto {
    private Long id;
    private Double ticketPrice;

    public EventDto(Long id, Double ticketPrice) {
        this.id = id;
        this.ticketPrice = ticketPrice;
    }

    public EventDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "id=" + id +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
