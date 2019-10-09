package com.two.authentication.exceptions;

import com.two.http_api.exceptions.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class HttpRequestExceptionMapper {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestExceptionMapper.class);

    /**
     * @param e Http Message Not Readable Exception, typically occurring if the response body is malformed or not present.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse error(HttpMessageNotReadableException e) throws ResponseStatusException {
        logger.warn("[400] Badly formed HTTP request received.", e);
        return new ExceptionResponse("Badly formed HTTP request.", HttpStatus.BAD_REQUEST);
    }

}
