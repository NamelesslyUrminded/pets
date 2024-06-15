package com.example.pets.exception;

public class EntityModifyException extends Exception {

    public EntityModifyException(String message) {
        super(message);
    }

    public EntityModifyException(String message, Object params) {
        super(String.format(message, params));
    }
}

