package co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions;

public class AccountDisabledException extends RuntimeException{
    public AccountDisabledException(){
        super("AccountDisabledException: The account is disabled");
    }
    public AccountDisabledException(String message){
        super("AccountDisabledException: The account of "+message+" is disabled");
    }
}
