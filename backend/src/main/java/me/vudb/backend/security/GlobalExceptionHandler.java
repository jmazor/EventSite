package me.vudb.backend.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception) {
        // You can use a custom error code here, for example 1001
        int errorCode = 1001;

        // Create a custom error response with the error code and the exception message
        ErrorResponse errorResponse = new ErrorResponse(errorCode, exception.getMessage());

        // Return the custom error response with an appropriate HTTP status
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
