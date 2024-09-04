package com.intuit.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<String> handleCustomException(ValidationException ex) {
        LOGGER.error("ValidationException occurred: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    @ResponseBody
    public ResponseEntity<String> handleMethodArgumentNotValidException(Exception ex) {
        LOGGER.error("Exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>("Wrong value entered for one of the parameters", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class,InterruptedException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(Exception ex) {
        LOGGER.error("Unexpected error occurred: {}", ex.getMessage());
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
