package co.com.icesi.TallerJpa.error.exception;

import lombok.Getter;

@Getter
public class IcesiException extends RuntimeException{
    private final IcesiError error;

    public IcesiException(String message, IcesiError error){
        super(message);
        this.error = error;
    }
}
