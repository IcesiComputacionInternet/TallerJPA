package co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions;

public class AccountUnableToTransferException extends RuntimeException{
    public AccountUnableToTransferException(){
        super("AccountUnableToTransferException: the transfer value is higher than the balance of the account");
    }
}
