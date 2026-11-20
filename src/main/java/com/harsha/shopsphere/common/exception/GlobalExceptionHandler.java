package com.harsha.shopsphere.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex ,
                                                                HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND , ex.getMessage(), request , Map.of());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex ,
                                                                 HttpServletRequest request) {
        return build(HttpStatus.CONFLICT , ex.getMessage() , request , Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex ,
                                                                      HttpServletRequest request) {

        Map<String , String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        fieldErrors.putIfAbsent(error.getField() , error.getDefaultMessage()));

        return build(HttpStatus.BAD_REQUEST , "Validation failed" , request , fieldErrors);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleBadInput(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Malformed or invalid request", request, Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrity(DataIntegrityViolationException ex,
                                                         HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Request conflicts with existing data", request, Map.of());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status ,
                                                String message ,
                                                HttpServletRequest request ,
                                                Map<String , String> fieldErrors) {
        ErrorResponse body = new ErrorResponse(
                Instant.now() ,
                status.value() ,
                status.getReasonPhrase() ,
                message ,
                request.getRequestURI() ,
                fieldErrors
        );

        return ResponseEntity.status(status).body(body);
    }
}
