package co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions;

public class AccountNumberAlreadyInUseException extends RuntimeException{
    public AccountNumberAlreadyInUseException(){
        super("AccountNumberAlreadyInUseException: The account number is already in use");
    }
}
