package co.com.icesi.demojpa.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IcesiException extends RuntimeException{

    private HttpStatus status;

    public IcesiException(String message) {
        super(message);
    }
}
