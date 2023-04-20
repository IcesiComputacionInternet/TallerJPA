package co.com.icesi.TallerJPA.error.exception;

import lombok.Getter;

@Getter
public class ArgumentsException extends RuntimeException{

    private ArgumentsError error;
    public ArgumentsException(String message,ArgumentsError error) {
        super(message);
        this.error = error;
    }

    public ArgumentsException(String message) {
        super(message);
    }
}
