package it.test.spring.jwt.mongodb.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.test.spring.jwt.mongodb.models.ErrorJson;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    protected ResponseEntity<ErrorJson> handleConflict(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorJson(HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}