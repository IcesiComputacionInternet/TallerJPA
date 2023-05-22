package co.com.icesi.TallerJPA.error;

import co.com.icesi.TallerJPA.error.exception.ApplicationError;
import co.com.icesi.TallerJPA.error.exception.ApplicationException;
import co.com.icesi.TallerJPA.error.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static co.com.icesi.TallerJPA.error.util.ErrorManager.createDetail;
import static co.com.icesi.TallerJPA.error.util.ErrorManager.sendDetails;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationError> handleApplicationException(ApplicationException exception){
        return ResponseEntity.status(exception.getError().getStatus()).body(exception.getError());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationError> handleMissArgumentException(MethodArgumentNotValidException exception){
        ApplicationError error = ApplicationError.builder().status(HttpStatus.BAD_REQUEST).details(List.of(sendDetails(createDetail(exception.getMessage(), ErrorCode.ERROR_MISSING_ARGUMENT)))).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApplicationError> handleAuthException(AuthenticationException exception){
        ApplicationError error = ApplicationError.builder().status(HttpStatus.UNAUTHORIZED).details(List.of(sendDetails(createDetail(exception.getMessage(), ErrorCode.ERROR_LOGIN)))).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationError> handleRuntimeException(RuntimeException exception){
        ApplicationError error = ApplicationError.builder().status(HttpStatus.BAD_REQUEST).details(List.of(sendDetails(createDetail(exception.getMessage(), ErrorCode.RUNTIME_ERROR)))).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
