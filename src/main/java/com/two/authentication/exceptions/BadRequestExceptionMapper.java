package com.two.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Collections.singletonList;

@RestControllerAdvice
public class BadRequestExceptionMapper {

    /**
     * @param e Constraint Violation Exception, typically raised by JavaX Validation Constraints.
     * @return a list of user-friendly errors, extracted from each constraint violation.
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse error(BadRequestException e) {
        return new ErrorResponse(
                singletonList(e.getMessage())
        );
    }

}
