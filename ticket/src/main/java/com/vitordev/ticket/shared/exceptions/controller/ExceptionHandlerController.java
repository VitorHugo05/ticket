package com.vitordev.ticket.shared.exceptions.controller;

import com.vitordev.ticket.shared.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(HttpServletRequest request, ObjectNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError(System.currentTimeMillis(), status.value(), "Object not found", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(BadRequestArgumentException.class)
    public ResponseEntity<StandardError> badRequest(HttpServletRequest request, BadRequestArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError(System.currentTimeMillis(), status.value(), "Bad request", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceExpiredException.class)
    public ResponseEntity<StandardError> resourceExpired(HttpServletRequest request, ResourceExpiredException e) {
        HttpStatus status = HttpStatus.GONE;
        StandardError error = new StandardError(System.currentTimeMillis(), status.value(), "Resource expired", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<StandardError> duplicateRequest(HttpServletRequest request, DuplicateRequestException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError error = new StandardError(System.currentTimeMillis(), status.value(), "Duplicate request", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InsufficientResourceException.class)
    public ResponseEntity<StandardError> insufficientResource(HttpServletRequest request, InsufficientResourceException e) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError error = new StandardError(System.currentTimeMillis(), status.value(), "Insufficient Resource", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}
