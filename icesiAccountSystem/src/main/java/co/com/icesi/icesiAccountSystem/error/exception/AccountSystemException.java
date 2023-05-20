package co.com.icesi.icesiAccountSystem.error.exception;

import lombok.Getter;

@Getter
public class AccountSystemException extends RuntimeException{
    private AccountSystemError error;
    public AccountSystemException(String message, AccountSystemError error) {
        super(message);
        this.error = error;
    }
}
