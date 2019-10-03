package com.two.authentication.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Collections.singletonList;

@RestControllerAdvice
public class BadRequestExceptionMapper {

    private static final Logger logger = LoggerFactory.getLogger(BadRequestExceptionMapper.class);

    /**
     * @param e Bad Request Exception, to be mapped to status 400.
     * @return a singular error within a list for consistency.
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse error(BadRequestException e) {
        logger.warn("[400] Mapping BadRequestException to 400 BAD REQUEST.", e);
        return new ErrorResponse(singletonList(e.getMessage()));
    }

}
