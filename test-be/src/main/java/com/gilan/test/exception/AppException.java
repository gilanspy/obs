package com.gilan.test.exception;

import com.gilan.test.model.ErrorType;

public class AppException extends RuntimeException {
	
    private static final long serialVersionUID = 1L; 
    private final ErrorType errorType;

    public AppException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
