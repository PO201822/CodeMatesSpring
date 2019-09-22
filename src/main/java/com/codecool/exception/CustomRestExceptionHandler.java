package com.codecool.exception;

import com.codecool.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidUserCredentialsException.class})
    public ResponseEntity<Object> handleInvalidUserCredentialsException(InvalidUserCredentialsException e) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage());
        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }
}