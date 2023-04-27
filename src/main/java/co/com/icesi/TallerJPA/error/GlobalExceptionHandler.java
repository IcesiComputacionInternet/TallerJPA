package co.com.icesi.TallerJPA.error;

import co.com.icesi.TallerJPA.error.enums.ErrorCode;
import co.com.icesi.TallerJPA.error.exception.ArgumentsError;
import co.com.icesi.TallerJPA.error.exception.ArgumentsException;
import co.com.icesi.TallerJPA.error.util.ArgumentsExceptionBuilder;
import co.com.icesi.TallerJPA.error.util.DetailBuilder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ArgumentsException.class)
    public ResponseEntity<ArgumentsError>handleMissingArgumentsException (ArgumentsException argumentsException){
        return ResponseEntity.status(argumentsException.getError().getStatus()).body(argumentsException.getError());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public HashMap<String, String> handleMissingArgumentsException(MethodArgumentNotValidException e) {
        HashMap<String, String> errors = new HashMap<>();

        e.getAllErrors().forEach((error) -> {
            String fieldName = ((DefaultMessageSourceResolvable)error.getArguments()[0]).getDefaultMessage();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ArgumentsError> handleRuntimeException(RuntimeException runtimeException){
        var error = ArgumentsExceptionBuilder.createArgumentsError(runtimeException.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,new DetailBuilder(ErrorCode.ERR_500));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }



}
