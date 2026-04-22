package com.vitordev.ticket.payments.model.dto;

import java.time.LocalDateTime;

public class PaymentFailedMessage {
    private Long paymentId;
    private Long orderId;
    private Double amount;
    private LocalDateTime failedAt;

    public PaymentFailedMessage(Long paymentId, Long orderId, Double amount, LocalDateTime failedAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.failedAt = failedAt;
    }

    public PaymentFailedMessage() {
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

    public LocalDateTime getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(LocalDateTime failedAt) {
        this.failedAt = failedAt;
    }
}
