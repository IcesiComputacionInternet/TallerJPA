package co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions;

public class AccountCantBeDisableException extends RuntimeException{
    public AccountCantBeDisableException(){
        super("AccountCantBeDisableException: account balance is higher than zero");
    }
}
