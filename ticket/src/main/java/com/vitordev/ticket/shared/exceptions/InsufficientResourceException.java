package com.vitordev.ticket.shared.exceptions;

public class InsufficientResourceException extends RuntimeException {
    public InsufficientResourceException(String message) {
        super(message);
    }
}
