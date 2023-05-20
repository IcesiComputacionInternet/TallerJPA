package co.com.icesi.tallerjpa.error;

import co.com.icesi.tallerjpa.error.exception.IcesiError;
import co.com.icesi.tallerjpa.error.exception.IcesiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<IcesiError> handleException(IcesiException documentException){
        return new ResponseEntity<>(documentException.getError(), documentException.getHttpStatus());
    }

}
