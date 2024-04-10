package com.example.pets.controller;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionSupervisor extends ResponseEntityExceptionHandler {

    public static final String ACCESS_DENIED = "Access denied!";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String FIELD_ERROR_SEPARATOR = "; ";
    private static final String PATH = "path";
    private static final String ERRORS = "error";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";
    private static final String TYPE = "type";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode statusCode,
                                                                  WebRequest request) {
        final List<String> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + FIELD_ERROR_SEPARATOR + error.getDefaultMessage())
                .collect(Collectors.toList());
        return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, request, validationErrors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, request,
                Collections.singletonList(exception.getLocalizedMessage()));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException exception, WebRequest request) {
        return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, request, Collections.singletonList(exception.getSQLException().getLocalizedMessage()));
    }

    @ExceptionHandler({PropertyValueException.class})
    public ResponseEntity<Object> handlePropertyValueException(
            PropertyValueException exception, WebRequest request) {
        String propertyValue = exception.getEntityName() + FIELD_ERROR_SEPARATOR + exception.getPropertyName();
        String message = exception.getLocalizedMessage();
        return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, request, Arrays.asList(propertyValue, message));
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(
            NullPointerException exp, WebRequest request){
        String exceptionValue = exp.getMessage();
        return getExceptionResponseEntity(exp, HttpStatus.BAD_REQUEST, request, Arrays.asList(exceptionValue));
    }
    private ResponseEntity<Object> getExceptionResponseEntity(Exception exception,
                                                              HttpStatus status,
                                                              WebRequest request,
                                                              List<String> errors) {
        final Map<String, Object> body = new LinkedHashMap<>();
        final String path = request.getDescription(false);
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, status.value());
        body.put(ERRORS, errors);
        body.put(TYPE, exception.getClass().getSimpleName());
        body.put(PATH, path);
        body.put(MESSAGE, getMessageForStatus(status));
        return new ResponseEntity<>(body, status);
    }

    private Object getMessageForStatus(HttpStatus status) {
        switch (status) {
            case UNAUTHORIZED:
                return ACCESS_DENIED;
            case BAD_REQUEST:
                return INVALID_REQUEST;
            default:
                return status.getReasonPhrase();
        }
    }
}
