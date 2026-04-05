package com.vitordev.ticket.shared.exceptions;

public class BadRequestArgumentException extends RuntimeException {
    public BadRequestArgumentException(String message) {
        super(message);
    }
}
