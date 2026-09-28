package com.example.tinyledger.exception;

import com.example.tinyledger.dto.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CentralExceptionHandler {

    @ExceptionHandler(InsufficientFundException.class)
    public ResponseEntity<TransactionResponse> handleInsufficientFunds(InsufficientFundException ex) {
        TransactionResponse body = new TransactionResponse(
                ex.getMessage(),
                UUID.randomUUID(),
                null,
                null,
                null,
                ZonedDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(body);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<TransactionResponse> handleInvalidInput(Exception ex) {
        String message = ex instanceof MethodArgumentNotValidException mex ?
                "Invalid User Input for field(s): " + mex.getBindingResult().getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(", "))
                : "Bad Request";
        TransactionResponse body = new TransactionResponse(
                message,
                UUID.randomUUID(),
                null,
                null,
                null,
                ZonedDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TransactionResponse> handleException(Exception ex) {
        TransactionResponse body = new TransactionResponse(
                "Internal Server Error, Please contact support.",
                UUID.randomUUID(),
                null,
                null,
                null,
                ZonedDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
