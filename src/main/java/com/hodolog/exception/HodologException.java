package com.hodolog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HodologException extends RuntimeException {

    public Map<String, String> validation = new HashMap<>();

    public HodologException(String message) {
        super(message);
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    public abstract HttpStatus getStatusCode();
}
