package co.com.icesi.TallerJPA.error.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends  RuntimeException{
    private final ApplicationError error;

    public ApplicationException(String message, ApplicationError error){
        super(message);
        this.error = error;
    }

}
