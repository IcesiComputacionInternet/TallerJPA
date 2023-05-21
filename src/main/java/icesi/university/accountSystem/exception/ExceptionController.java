package icesi.university.accountSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = CustomException.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleCustomException(CustomException e){
        e.printStackTrace();
        return e.getErr_code() + ": "+ e.getMessage();
    }

    @ExceptionHandler(value = ExistsException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public String handleUserExistsException(ExistsException e){
        e.printStackTrace();
        return e.getMessage();
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public String handleNotBadRequestException(BadRequestException e){
        e.printStackTrace();
        return e.getMessage();
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedException(UnauthorizedException e){
        e.printStackTrace();
        return e.getMessage();
    }
}
