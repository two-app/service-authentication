package com.two.authentication.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String reason) {
        super(reason);
    }

}
