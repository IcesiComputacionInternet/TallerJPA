package co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions;

public class AccountBalanceNotValidException extends RuntimeException{
    public AccountBalanceNotValidException(String message){
        super("AccountBalanceNotValidException: "+ message);
    }
}
