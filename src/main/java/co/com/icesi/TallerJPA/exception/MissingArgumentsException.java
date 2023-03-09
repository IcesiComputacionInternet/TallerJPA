package co.com.icesi.TallerJPA.exception;

public class MissingArgumentsException extends RuntimeException{
    public MissingArgumentsException(String message) {
        super(message);
    }
}
