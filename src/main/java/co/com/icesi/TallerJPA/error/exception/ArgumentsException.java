package co.com.icesi.TallerJPA.error.exception;

import lombok.Getter;

public class ArgumentsException extends RuntimeException{

    @Getter
    private final ArgumentsError error;
    public ArgumentsException(String message,ArgumentsError error) {
        super(message);
        this.error = error;
    }
}
