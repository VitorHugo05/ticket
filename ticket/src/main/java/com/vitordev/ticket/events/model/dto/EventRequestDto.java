package com.vitordev.ticket.events.model.dto;
import java.time.LocalDateTime;

public class EventRequestDto {

    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double ticketPrice;

    private Integer tickets;

    public EventRequestDto(String name, String description, LocalDateTime startTime, LocalDateTime endTime, Double ticketPrice, Integer tickets) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketPrice = ticketPrice;
        this.tickets = tickets;
    }

    public EventRequestDto() {
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getTickets() {
        return tickets;
    }

    public void setTickets(Integer tickets) {
        this.tickets = tickets;
    }
}
