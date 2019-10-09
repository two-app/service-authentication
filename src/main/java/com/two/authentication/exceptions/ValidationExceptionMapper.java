package com.two.authentication.exceptions;

import com.two.http_api.exceptions.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ValidationExceptionMapper {

    private static final Logger logger = LoggerFactory.getLogger(ValidationExceptionMapper.class);

    /**
     * @param e Constraint Violation Exception, typically raised by JavaX Validation Constraints.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse error(ConstraintViolationException e) {
        logger.warn("[400] Converting multiple Constraint Violations into 400 BAD REQUEST.", e);
        return new ExceptionResponse(
                e.getConstraintViolations().iterator().next().getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * @param e Method Argument Not Valid Exception, typically raised by JavaX @Valid annotation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse error(MethodArgumentNotValidException e) {
        logger.warn("[400] Converting singular Constraint Violation into 400 BAD REQUEST.", e);
        return new ExceptionResponse(
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
    }

}
