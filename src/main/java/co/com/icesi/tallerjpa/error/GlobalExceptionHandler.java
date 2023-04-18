package co.com.icesi.tallerjpa.error;

import co.com.icesi.tallerjpa.error.exception.CustomError;
import co.com.icesi.tallerjpa.error.exception.CustomException;
import co.com.icesi.tallerjpa.error.enums.ErrorCode;
import co.com.icesi.tallerjpa.error.util.DetailBuilder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import static co.com.icesi.tallerjpa.error.util.ExceptionBuilder.createCustomError;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ex.getAllErrors().stream()
                .collect(
                        HashMap::new,
                        (m, e) -> m.put(((DefaultMessageSourceResolvable) Objects.requireNonNull(e.getArguments())[0]).getDefaultMessage(), e.getDefaultMessage()),
                        HashMap::putAll
                );
    }
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<CustomError> handleCustomException(CustomException exception){
        return ResponseEntity.status(exception.getError().getStatus()).body(exception.getError());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomError> handleRuntimeException(RuntimeException runtimeException){
        var error = createCustomError(runtimeException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, new DetailBuilder(ErrorCode.ERR_500));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


}
