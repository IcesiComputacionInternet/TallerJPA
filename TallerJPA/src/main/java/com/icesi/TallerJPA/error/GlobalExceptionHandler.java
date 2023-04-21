package com.icesi.TallerJPA.error;

import com.icesi.TallerJPA.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static com.icesi.TallerJPA.error.util.IcesiExceptionBuilder.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiError> handleIcesiException(IcesiException e) {
        return ResponseEntity.status(e.getError().getStatus()).body(e.getError());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<IcesiError> handleRuntimeException(RuntimeException e) {
        var error = createIcesiError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new DetailBuilder(ErrorCode.ERR_500));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}



























