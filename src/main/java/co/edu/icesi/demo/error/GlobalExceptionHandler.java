package co.edu.icesi.demo.error;

import co.edu.icesi.demo.error.exception.IcesiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiException> handleIcesiException(IcesiException icesiException){
        //return ResponseEntity.status(icesiException.getError().getStatus()).body(icesiException.getError());
        return null;
    }
}
