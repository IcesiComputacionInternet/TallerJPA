package co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions;

public class UserNotExistsException extends RuntimeException{
    public UserNotExistsException(){
        super("UserNotExistsException: The user entered does not exist");
    }
}
