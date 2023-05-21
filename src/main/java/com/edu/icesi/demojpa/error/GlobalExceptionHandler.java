package com.edu.icesi.demojpa.error;

import com.edu.icesi.demojpa.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.edu.icesi.demojpa.error.util.IcesiExceptionBuilder.createIcesiError;

public class GlobalExceptionHandler {

    @ExceptionHandler(IcesiException.class)
    public ResponseEntity<IcesiError> handleIcesiException(IcesiException icesiException){
        return ResponseEntity.status(icesiException.getError().getStatus()).body(icesiException.getError());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<IcesiError> handleRuntimeException(RuntimeException runtimeException){
        var error = createIcesiError(HttpStatus.INTERNAL_SERVER_ERROR, new DetailBuilder(ErrorCode.ERROR_500));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ResponseEntity<IcesiError> handleValidationException(MethodArgumentNotValidException exception){
        var errorBuilder = IcesiError.builder().status(HttpStatus.BAD_REQUEST);
        var details = exception.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var error = errorBuilder.details(details).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    private IcesiErrorDetail mapBindingResultToError(ObjectError objectError) {
        var message = ErrorCode.ERROR_400.getMessage().formatted(((FieldError) objectError).getField(), objectError.getDefaultMessage());
        return IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERROR_400.getCode())
                .errorMessage(message)
                .build();
    }
}
