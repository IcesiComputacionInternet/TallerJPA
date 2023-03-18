package co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions;

public class AccountUnableToWithdrawalException extends RuntimeException{
    public AccountUnableToWithdrawalException(String message){
        super("AccountUnableToWithdrawalException: "+message);
    }
}
