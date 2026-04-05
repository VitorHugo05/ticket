package com.vitordev.ticket.shared.exceptions.controller;

import com.vitordev.ticket.shared.exceptions.BadRequestArgumentException;
import com.vitordev.ticket.shared.exceptions.ObjectNotFoundException;
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
}
