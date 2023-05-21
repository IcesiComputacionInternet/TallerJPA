package com.example.tallerjpa.error.exception;


import com.example.tallerjpa.error.util.CustomError;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private CustomError error;
    public CustomException(CustomError error) {
        super();
        this.error = error;
    }

    public CustomException(String message) {
        super(message);
    }
}
