package co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions;

public class AccountTypeNotExistsException extends RuntimeException{
    public AccountTypeNotExistsException(String message){
        super("AccountTypeNotExistsException: "+message+" not exists");
    }
}
