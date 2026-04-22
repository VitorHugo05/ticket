package com.vitordev.ticket.shared.exceptions;

public class ResourceExpiredException extends RuntimeException {
    public ResourceExpiredException(String message) {
        super(message);
    }
}
