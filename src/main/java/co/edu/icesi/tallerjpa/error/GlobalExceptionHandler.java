package co.edu.icesi.tallerjpa.error;

import co.edu.icesi.tallerjpa.error.exception.DetailBuilder;
import co.edu.icesi.tallerjpa.error.exception.ErrorCode;
import co.edu.icesi.tallerjpa.error.exception.IcesiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import co.edu.icesi.tallerjpa.error.exception.IcesiErrorDetail;

import static co.edu.icesi.tallerjpa.error.util.IcesiExceptionBuilder.createIcesiError;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IcesiError> handleValidationExceptions(
            MethodArgumentNotValidException argumentNotValidException) {
        var errorBuilder = IcesiError.builder().status(HttpStatus.BAD_REQUEST);
        var details = argumentNotValidException.getBindingResult().getAllErrors().stream().map(this::mapBindingResultToError).toList();
        var error = createIcesiError(argumentNotValidException.getMessage(), HttpStatus.BAD_REQUEST, new DetailBuilder(ErrorCode.ERR_400));
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    private IcesiErrorDetail mapBindingResultToError(ObjectError objectError){
        var message = ErrorCode.ERR_400.getMessage().formatted(((FieldError) objectError).getField(), objectError.getDefaultMessage());
        return IcesiErrorDetail.builder()
                .errorCode(ErrorCode.ERR_400.getCode())
                .errorMessage(message).build();
    }
}
