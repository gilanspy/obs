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
import com.gilan.test.model.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<String>> handleAppException(AppException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex.getErrorType() == ErrorType.ITEM_NOT_FOUND || ex.getErrorType() == ErrorType.ORDER_NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        }

        ApiResponse<String> response = new ApiResponse<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        
        ApiResponse<Map<String, String>> response = new ApiResponse<>("error", "Validation failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}