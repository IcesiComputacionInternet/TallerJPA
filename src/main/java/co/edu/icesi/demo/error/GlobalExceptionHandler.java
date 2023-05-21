package co.edu.icesi.demo.error;

import co.edu.icesi.demo.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static co.edu.icesi.demo.error.util.IcesiExceptionBuilder.createIcesiError;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiError> handleIcesiException(IcesiException icesiException){
        return ResponseEntity.status(icesiException.getError().getStatus()).body(icesiException.getError());
    }
/*
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<IcesiError> handleRuntimeException(RuntimeException runtimeException){
        var error = createIcesiError(runtimeException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new DetailBuilder(ErrorCode.ERR_500));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

*/
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<IcesiError> handleAuthenticationException(AuthenticationException authenticationException){
        var error = createIcesiError(authenticationException.getMessage(), HttpStatus.UNAUTHORIZED, new DetailBuilder(ErrorCode.ERR_LOGIN,"Login unauthorized"));
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IcesiError> handleValidationExceptions( MethodArgumentNotValidException ex) {
        var errorBuilder = IcesiError.builder().status(HttpStatus.BAD_REQUEST);
        var details = ex.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    private IcesiErrorDetail mapBindingResultToError(ObjectError objectError){
        var message = ErrorCode.ERR_400.getMessage().formatted(((FieldError) objectError).getField(), objectError.getDefaultMessage());
        return IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(message).build();
    }

}