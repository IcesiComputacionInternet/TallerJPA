package com.example.jpa.error;

import com.example.jpa.error.exceptions.AccountException;
import com.example.jpa.error.exceptions.RoleException;
import com.example.jpa.error.exceptions.UserException;
import com.example.jpa.error.exceptions.ValidationException;
import com.example.jpa.error.util.IcesiError;
import com.example.jpa.error.util.IcesiErrorCode;
import com.example.jpa.error.util.IcesiErrorDetail;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<IcesiError> handleRuntimeException(RuntimeException runtimeException){
        runtimeException.printStackTrace();
        var error = IcesiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .details(List.of(IcesiErrorDetail.builder()
                        .errorCode(IcesiErrorCode.ERR_500)
                        .errorMessage("Oops, we ran into an error")
                        .build()))
                .build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<IcesiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        exception.printStackTrace();
        var errorsDetails = exception.getBindingResult().getAllErrors().stream()
                .map(this::mapErrorToIcesiErrorDetail).toList();
        var error = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(errorsDetails)
                .build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    //Private method to map an Object Error to IcesiErrorDetail
    private IcesiErrorDetail mapErrorToIcesiErrorDetail(ObjectError error){
        var field = ((DefaultMessageSourceResolvable) Objects.requireNonNull(error.getArguments())[0]).getDefaultMessage();
        return IcesiErrorDetail.builder()
                .errorCode(IcesiErrorCode.valueOf(error.getCode()))
                .errorMessage(field + error.getDefaultMessage())
                .build();
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<IcesiError> handleAccountException(AccountException accountException){
        IcesiErrorDetail errorDetail = accountException.getIcesiError().getDetails().get(0);
        List<IcesiErrorDetail> listOfErrors = accountException.getIcesiError().getDetails();
        var argumentsError = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(listOfErrors)
                .build();
        return ResponseEntity.badRequest().body(argumentsError);
    }

    @ExceptionHandler(RoleException.class)
    public ResponseEntity<IcesiError> handleRoleException(RoleException roleException){
        IcesiErrorDetail errorDetail = roleException.getIcesiError().getDetails().get(0);
        List<IcesiErrorDetail> listOfErrors = roleException.getIcesiError().getDetails();
        var argumentsError = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(listOfErrors)
                .build();
        return ResponseEntity.badRequest().body(argumentsError);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<IcesiError> handleUserException(UserException userException){
        IcesiErrorDetail errorDetail = userException.getIcesiError().getDetails().get(0);
        List<IcesiErrorDetail> listOfErrors = userException.getIcesiError().getDetails();
        var argumentsError = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(listOfErrors)
                .build();
        return ResponseEntity.badRequest().body(argumentsError);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<IcesiError> handleValidationException(ValidationException validationException){
        IcesiErrorDetail errorDetail = validationException.getIcesiError().getDetails().get(0);
        List<IcesiErrorDetail> listOfErrors = validationException.getIcesiError().getDetails();
        var argumentsError = IcesiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .details(listOfErrors)
                .build();
        return ResponseEntity.badRequest().body(argumentsError);
    }

}
