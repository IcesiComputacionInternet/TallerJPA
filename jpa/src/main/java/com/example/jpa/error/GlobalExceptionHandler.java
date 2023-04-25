package com.example.jpa.error;

import com.example.jpa.error.exceptions.AccountException;
import com.example.jpa.error.exceptions.RoleException;
import com.example.jpa.error.exceptions.UserException;
import com.example.jpa.error.exceptions.ValidationException;
import com.example.jpa.error.util.IcesiError;
import com.example.jpa.error.util.IcesiErrorDetail;
import com.example.jpa.exceptions.AccountNotFoundException;
import com.example.jpa.exceptions.AccountTypeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<IcesiError> handleRuntimeException(RuntimeException runtimeException){
        runtimeException.printStackTrace();
        var error = IcesiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .details(List.of(IcesiErrorDetail.builder()
                        .errorCode("ERR_500")
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
        return IcesiErrorDetail.builder()
                .errorCode(error.getCode())
                .errorMessage(error.getDefaultMessage())
                .build();
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<String> handleAccountException(AccountException accountException){
        accountException.printStackTrace();
        return ResponseEntity.badRequest().body(accountException.getMessage());
    }

    @ExceptionHandler(RoleException.class)
    public ResponseEntity<String> handleRoleException(RoleException roleException){
        roleException.printStackTrace();
        return ResponseEntity.badRequest().body(roleException.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException userException){
        userException.printStackTrace();
        return ResponseEntity.badRequest().body(userException.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException validationException){
        validationException.printStackTrace();
        return ResponseEntity.badRequest().body(validationException.getMessage());
    }

}
