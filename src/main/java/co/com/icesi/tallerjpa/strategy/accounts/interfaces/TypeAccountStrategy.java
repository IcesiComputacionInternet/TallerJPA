package co.com.icesi.tallerjpa.strategy.accounts.interfaces;

import co.com.icesi.tallerjpa.enums.TypeAccount;
import co.com.icesi.tallerjpa.model.Account;

import java.util.UUID;

public interface TypeAccountStrategy {

    TypeAccount getType();
    void withdraw(Long amount, Account account);
    void transfer(Long amount, Account accountOrigin, Account accountDestination, boolean isReceiverAccountValid);
    boolean isReceiverAccountValid();
    void deposit(Long amount, Account account);

}
