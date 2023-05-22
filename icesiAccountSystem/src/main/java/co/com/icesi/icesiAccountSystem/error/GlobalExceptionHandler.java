package co.com.icesi.icesiAccountSystem.error;

import co.com.icesi.icesiAccountSystem.enums.ErrorCode;
import co.com.icesi.icesiAccountSystem.error.exception.AccountSystemError;
import co.com.icesi.icesiAccountSystem.error.exception.AccountSystemErrorDetail;
import co.com.icesi.icesiAccountSystem.error.exception.AccountSystemException;
import co.com.icesi.icesiAccountSystem.error.exception.DetailBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.ArrayList;

import static co.com.icesi.icesiAccountSystem.error.util.AccountSystemExceptionBuilder.createAccountSystemError;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AccountSystemException.class)
    public ResponseEntity<AccountSystemError> handleAccountSystemException(AccountSystemException icesiException){
        return ResponseEntity.status(icesiException.getError().getStatus()).body(icesiException.getError());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AccountSystemError> handleRuntimeException(RuntimeException runtimeException){
        var error = createAccountSystemError(runtimeException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new DetailBuilder(ErrorCode.ERR_500));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AccountSystemError> handleBadCredentialsException(BadCredentialsException exception){
        var error = createAccountSystemError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new DetailBuilder(ErrorCode.ERR_501));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<AccountSystemError> handleValidationExceptions( MethodArgumentNotValidException ex) {
        var errorBuilder = AccountSystemError.builder().status(HttpStatus.BAD_REQUEST);
        var details = ex.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<AccountSystemError> handleValidationExceptions1(ConstraintViolationException ex) {
        var errorBuilder = AccountSystemError.builder().status(HttpStatus.BAD_REQUEST);
        var details = ex.getConstraintViolations().stream().map(this::mapConstraintViolationToError).toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    private AccountSystemErrorDetail mapConstraintViolationToError(ConstraintViolation<?> violation) {
        String field = violation.getPropertyPath().toString();
        String message = violation.getMessage();
        return AccountSystemErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(field+message)
                .build();
    }

    private AccountSystemErrorDetail mapBindingResultToError(ObjectError objectError){
        var message = ErrorCode.ERR_400.getMessage().formatted(((FieldError) objectError).getField(), objectError.getDefaultMessage());
        return AccountSystemErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(message).build();
    }
/*
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    private ResponseEntity<AccountSystemError> handleValidationExceptions(Exception ex) {
        var errorBuilder = AccountSystemError.builder().status(HttpStatus.BAD_REQUEST);
        var details = new ArrayList<AccountSystemErrorDetail>();

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            details = (ArrayList<AccountSystemErrorDetail>) methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                    .map(this::mapFieldErrorToErrorDetail)
                    .toList();
        } else if (ex instanceof BindException) {
            BindException bindException = (BindException) ex;
            details = (ArrayList<AccountSystemErrorDetail>) bindException.getBindingResult().getFieldErrors().stream()
                    .map(this::mapFieldErrorToErrorDetail)
                    .toList();
        } else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;
            details = (ArrayList<AccountSystemErrorDetail>) constraintViolationException.getConstraintViolations().stream()
                    .map(this::mapConstraintViolationToErrorDetail)
                    .toList();
        }

        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    private AccountSystemErrorDetail mapFieldErrorToErrorDetail(FieldError fieldError) {
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();
        return AccountSystemErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(field + ": " + message)
                .build();
    }

    private AccountSystemErrorDetail mapConstraintViolationToErrorDetail(ConstraintViolation<?> constraintViolation) {
        String field = constraintViolation.getPropertyPath().toString();
        String message = constraintViolation.getMessage();
        return AccountSystemErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(field + ": " + message)
                .build();
    }
*/
}
