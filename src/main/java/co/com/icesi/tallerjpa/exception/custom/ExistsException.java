package co.com.icesi.tallerjpa.exception.custom;

public class ExistsException extends RuntimeException {
    public ExistsException(String message) {
        super(message);
    }
}
