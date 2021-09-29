package com.army.swf.checkpoint.helpers;

import com.army.swf.checkpoint.exceptions.InvalidIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);

    @ExceptionHandler(value = {InvalidIdException.class})
    public ResponseEntity<Object> handleInvalidIdException(InvalidIdException ex) {
        logger.error("Invalid ID Exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
