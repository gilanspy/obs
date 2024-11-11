package com.gilan.test.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gilan.test.model.ErrorType;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, String>> handleAppException(AppException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("errorType", ex.getErrorType().name());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex.getErrorType() == ErrorType.INSUFFICIENT_STOCK) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex.getErrorType() == ErrorType.ITEM_NOT_FOUND || ex.getErrorType() == ErrorType.ORDER_NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
