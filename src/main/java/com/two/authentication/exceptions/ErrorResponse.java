package com.two.authentication.exceptions;

import lombok.Value;

import java.util.List;

@Value
public class ErrorResponse {
    private final List<String> errors;
}
