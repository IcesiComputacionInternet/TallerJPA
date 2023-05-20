package com.icesi.TallerJPA.error;

import com.icesi.TallerJPA.error.exception.*;
import com.icesi.TallerJPA.error.util.IcesiExceptionBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static com.icesi.TallerJPA.error.util.IcesiExceptionBuilder.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final IcesiExceptionBuilder eb = new IcesiExceptionBuilder();

    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiError> handleIcesiException(IcesiException e) {
        return ResponseEntity.status(e.getError().getStatus()).body(e.getError());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<IcesiError> handleRuntimeException(RuntimeException e) {
        var error = createIcesiError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new DetailBuilder(ErrorCode.ERR_500));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<IcesiError> handleIllegalArgumentException(IllegalArgumentException e) {
        var error = createIcesiError(e.getMessage(), HttpStatus.BAD_REQUEST, new DetailBuilder(ErrorCode.ERR_ILLEGAL));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<IcesiError> handleBadCredentialsException(BadCredentialsException e){
        var error = createIcesiError("Bad Credentials", HttpStatus.UNAUTHORIZED, new DetailBuilder(ErrorCode.ERR_UNAUTHORIZED));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IcesiError> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(eb.exceptionMethodArgumentValid("", e.getMessage()));
    }
}



























