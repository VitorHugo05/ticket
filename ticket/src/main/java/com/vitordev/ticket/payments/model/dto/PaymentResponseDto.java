package com.vitordev.ticket.payments.model.dto;

import com.vitordev.ticket.payments.model.enums.PaymentStatus;

import java.io.Serializable;

public class PaymentResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long timestamp;
    private PaymentStatus status;
    private String message;

    public PaymentResponseDto(Long timestamp, PaymentStatus status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
