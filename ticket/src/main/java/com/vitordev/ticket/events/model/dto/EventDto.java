package com.vitordev.ticket.events.model.dto;

import java.io.Serializable;

public class EventDto implements Serializable {
    private static final long serialVersionUID = 1L;

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
}
