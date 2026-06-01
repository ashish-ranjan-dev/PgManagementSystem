package com.pgmanagement.authservice.exception;

import com.pgmanagement.authservice.dto.ErrorResponse;
import com.pgmanagement.authservice.exception.AuthExceptions.InvalidCredentialsException;
import com.pgmanagement.authservice.exception.AuthExceptions.PgServiceException;
import com.pgmanagement.authservice.exception.AuthExceptions.PhoneAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .toList();

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                "One or more fields are invalid",
                details
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                ex.getMessage(),
                null
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePhoneExists(PhoneAlreadyExistsException ex) {
        log.info("Signup rejected: phone already registered");
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Phone number already registered",
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(PgServiceException.class)
    public ResponseEntity<ErrorResponse> handlePgService(PgServiceException ex) {
        log.error("PG service failure during signup", ex);
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                "Signup could not be completed at this time. Please try again.",
                null
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private String formatFieldError(FieldError fe) {
        return fe.getField() + ": " + fe.getDefaultMessage();
    }
}