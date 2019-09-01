package com.two.authentication.exceptions;

import lombok.Value;

import java.util.List;

@Value
class ErrorResponse {
    private final List<String> errors;
}
