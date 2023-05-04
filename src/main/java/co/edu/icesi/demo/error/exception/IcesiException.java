package co.edu.icesi.demo.error.exception;

import lombok.Getter;

@Getter
public class IcesiException extends RuntimeException {

    public IcesiException(String message) {
        super(message);
    }

}
