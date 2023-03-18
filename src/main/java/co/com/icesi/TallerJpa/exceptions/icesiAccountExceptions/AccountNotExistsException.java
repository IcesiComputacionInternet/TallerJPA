package co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions;

public class AccountNotExistsException extends RuntimeException{
    public AccountNotExistsException(String message){
        super("AccountNotExistsException: There is not an account with that "+message);
    }
}
