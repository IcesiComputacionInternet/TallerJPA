package co.com.icesi.TallerJpa.exceptions.icesiUserExceptions;

public class UserAttributeAlreadyInUseException extends RuntimeException {
    public UserAttributeAlreadyInUseException(String message){
        super("AttributeAlreadyInUseException: "+message);
    }

}
