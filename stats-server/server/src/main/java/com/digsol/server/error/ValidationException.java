package com.digsol.server.error;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

}
