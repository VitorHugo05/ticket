package com.vitordev.ticket.payments.model.dto;

import java.time.LocalDateTime;

public class PaymentApprovedMessage {
    private Long paymentId;
    private Long orderId;
    private Double amount;
    private LocalDateTime approvedAt;

    public PaymentApprovedMessage(Long paymentId, Long orderId, Double amount, LocalDateTime approvedAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.approvedAt = approvedAt;
    }

    public PaymentApprovedMessage() {
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
}
