package co.com.icesi.TallerJPA.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MissingArgumentsController {

    @ExceptionHandler(value = MissingArgumentsException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String handleMissingArgumentsException(MissingArgumentsException e) {
        e.printStackTrace();
        return e.getMessage();
    }


}
