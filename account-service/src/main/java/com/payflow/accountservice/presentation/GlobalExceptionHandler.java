package com.payflow.accountservice.presentation;

import com.payflow.accountservice.domain.exception.AccountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<Map<String, Object>> handleAccountException(
            AccountException ex) {

        HttpStatus status = switch (ex) {
            case AccountException.NotFound e         -> HttpStatus.NOT_FOUND;
            case AccountException.InsufficientFunds e -> HttpStatus.UNPROCESSABLE_ENTITY;
            case AccountException.AccountInactive e  -> HttpStatus.FORBIDDEN;
            case AccountException.DuplicateAccount e -> HttpStatus.CONFLICT;
        };

        return ResponseEntity.status(status).body(Map.of(
                "status", status.value(),
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}