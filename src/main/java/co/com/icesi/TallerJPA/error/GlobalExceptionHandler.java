package co.com.icesi.TallerJPA.error;

import co.com.icesi.TallerJPA.error.enums.ErrorCode;
import co.com.icesi.TallerJPA.error.exception.ArgumentsError;
import co.com.icesi.TallerJPA.error.exception.ArgumentsErrorDetail;
import co.com.icesi.TallerJPA.error.exception.ArgumentsException;
import co.com.icesi.TallerJPA.error.util.ArgumentsExceptionBuilder;
import co.com.icesi.TallerJPA.error.util.DetailBuilder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ArgumentsException.class)
    public ResponseEntity<ArgumentsError>handleMissingArgumentsException (ArgumentsException argumentsException){
        return ResponseEntity.status(argumentsException.getError().getStatus()).body(argumentsException.getError());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ArgumentsError> handleMissingArgumentsException(MethodArgumentNotValidException e) {
        var listOfErrors = e.getAllErrors().stream().map(this::mapErrorToArgumentsErrorDetail).toList();
        var argumentsError = ArgumentsError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(listOfErrors)
                .build();
        return ResponseEntity.status(argumentsError.getStatus()).body(argumentsError);
    }

    private ArgumentsErrorDetail mapErrorToArgumentsErrorDetail(ObjectError objectError){
        var field = ((DefaultMessageSourceResolvable) Objects.requireNonNull(objectError.getArguments())[0]).getDefaultMessage();
        var message = objectError.getDefaultMessage();
        return ArgumentsErrorDetail.builder()
                .errorCode("ERR_400")
                .errorMessage(message)
                .build();
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ArgumentsError> handleRuntimeException(RuntimeException runtimeException){
        var error = ArgumentsExceptionBuilder.createArgumentsError(runtimeException.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,new DetailBuilder(ErrorCode.ERR_500));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }



}
