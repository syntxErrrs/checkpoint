package com.army.swf.checkpoint.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidIdException extends Exception {
    public InvalidIdException(String errorMessage) {
        super(errorMessage);
    }
}
