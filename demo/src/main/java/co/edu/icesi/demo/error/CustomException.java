package co.edu.icesi.demo.error;

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
