package co.com.icesi.demojpa.error;
import co.com.icesi.demojpa.error.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static co.com.icesi.demojpa.error.util.IcesiExceptionBuilder.createIcesiError;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiError> handleIcesiException(IcesiException icesiException){
        return ResponseEntity.status(icesiException.getError().getStatus()).body(icesiException.getError());
    }

}
