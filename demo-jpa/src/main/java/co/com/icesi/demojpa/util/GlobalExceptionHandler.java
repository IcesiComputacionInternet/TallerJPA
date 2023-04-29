package co.com.icesi.demojpa.util;

import co.com.icesi.demojpa.exception.IcesiException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiException> handleIcesiException(IcesiException e){
        return ResponseEntity.status(e.getStatus()).body(e);
    }

}
