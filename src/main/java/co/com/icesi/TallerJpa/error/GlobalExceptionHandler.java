package co.com.icesi.TallerJpa.error;

import co.com.icesi.TallerJpa.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IcesiError> handleValidationExceptions( MethodArgumentNotValidException ex) {
        var errorBuilder = IcesiError.builder().status(HttpStatus.BAD_REQUEST);
        var details = ex.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<IcesiError> handleBindException(BindException ex) {
        var errorBuilder = IcesiError.builder().status(HttpStatus.BAD_REQUEST);
        var details = ex.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<IcesiError> handleConstraintViolationException(ConstraintViolationException ex){
        var errorBuilder = IcesiError.builder().status(HttpStatus.BAD_REQUEST);
        var details = ex.getConstraintViolations().stream()
                .map(violation -> mapNewResultToError(new ObjectError("role", violation.getMessage())))
                .toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<IcesiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex){
        var errorBuilder = IcesiError.builder().status(HttpStatus.BAD_REQUEST);
        var details = mapNewResultToError(new ObjectError("type","has a not valid account type"));
        var error = errorBuilder.details(List.of(details)).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<IcesiError> handleLoginException(BadCredentialsException ex){
        var errorBuilder = IcesiError.builder().status(HttpStatus.UNAUTHORIZED);
        var details = new IcesiErrorDetail(ErrorCode.ERR_401.getCode(), ex.getMessage());
        var error = errorBuilder.details(List.of(details)).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<IcesiError> handleAccessDeniedException(AccessDeniedException ex){
        var errorBuilder = IcesiError.builder().status(HttpStatus.FORBIDDEN);
        var details = new IcesiErrorDetail(ErrorCode.ERR_403.getCode(), ex.getMessage());
        var error = errorBuilder.details(List.of(details)).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<IcesiError> handleClassCastException(){
        var errorBuilder = IcesiError.builder().status(HttpStatus.UNAUTHORIZED);
        var details = new IcesiErrorDetail(ErrorCode.ERR_401.getCode(), "Tienes que autenticarte primero.");
        var error = errorBuilder.details(List.of(details)).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiError> handleIcesiException(IcesiException icesiException){
        return ResponseEntity.status(icesiException.getError().getStatus()).body(icesiException.getError());
    }

    private IcesiErrorDetail mapBindingResultToError(ObjectError objectError){
        var message = ErrorCode.ERR_400.getMessage().formatted(((FieldError) objectError).getField(), objectError.getDefaultMessage());
        return IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(message).build();
    }
    private IcesiErrorDetail mapNewResultToError(ObjectError objectError){
        var message = ErrorCode.ERR_400.getMessage().formatted(objectError.getObjectName(), objectError.getDefaultMessage());
        return IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(message).build();
    }
}
