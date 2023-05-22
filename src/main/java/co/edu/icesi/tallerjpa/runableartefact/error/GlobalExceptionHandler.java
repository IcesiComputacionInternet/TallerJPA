package co.edu.icesi.tallerjpa.runableartefact.error;

import co.edu.icesi.tallerjpa.runableartefact.error.enums.ErrorCode;
import co.edu.icesi.tallerjpa.runableartefact.error.exception.CustomException;
import co.edu.icesi.tallerjpa.runableartefact.error.util.CustomDetail;
import co.edu.icesi.tallerjpa.runableartefact.error.util.CustomError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        var details = ex.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var customError = CustomError.builder().status(HttpStatus.BAD_REQUEST).details(details).build();
        return ResponseEntity.status(customError.getStatus()).body(customError);
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<String> handleUserExistsException(CustomException e){
        e.printStackTrace();
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException runtimeException){
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(runtimeException.getMessage());
    }

    private CustomDetail mapBindingResultToError(ObjectError objectError){
        var field = ((DefaultMessageSourceResolvable) Objects.requireNonNull(objectError.getArguments())[0]).getDefaultMessage();
        var message = ErrorCode.ERR_400.getMessage().formatted(field, objectError.getDefaultMessage());
        return CustomDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(message).build();
    }

}
