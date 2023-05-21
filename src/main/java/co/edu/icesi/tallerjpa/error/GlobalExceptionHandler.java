package co.edu.icesi.tallerjpa.error;

import co.edu.icesi.tallerjpa.error.exception.ErrorCode;
import co.edu.icesi.tallerjpa.error.exception.IcesiError;
import co.edu.icesi.tallerjpa.error.exception.IcesiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import co.edu.icesi.tallerjpa.error.exception.IcesiErrorDetail;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IcesiError> handleValidationException(
            MethodArgumentNotValidException argumentNotValidException) {
        var errorBuilder = IcesiError.builder().status(HttpStatus.BAD_REQUEST);
        var details = argumentNotValidException.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<IcesiError> handleBindException(
            BindException argumentNotValidException) {
        var errorBuilder = IcesiError.builder().status(HttpStatus.BAD_REQUEST);
        var details = argumentNotValidException.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    private IcesiErrorDetail mapBindingResultToError(ObjectError objectError){
        var message = ErrorCode.ERR_400.getMessage().formatted(((FieldError) objectError).getField(), objectError.getDefaultMessage());
        return IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(message).build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<IcesiError> handleLoginException(
            BadCredentialsException badCredentialsException) {
        var errorBuilder = IcesiError.builder().status(HttpStatus.UNAUTHORIZED);
        List<IcesiErrorDetail> details = new ArrayList<>();
        details.add(new IcesiErrorDetail("401", badCredentialsException.getMessage()));
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiError> handleIcesiException(IcesiException icesiException){
        var error = icesiException.getError();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

}
