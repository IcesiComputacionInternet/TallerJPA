package co.com.icesi.tallerjpa.error.exception;

import lombok.Getter;


@Getter
public class CustomException extends RuntimeException {

    private final CustomError error;

    public CustomException(String message, CustomError error) {
        super(message);
        this.error = error;
    }



}
