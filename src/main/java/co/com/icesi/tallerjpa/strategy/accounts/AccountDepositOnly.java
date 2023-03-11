package co.com.icesi.tallerjpa.strategy.accounts;

import co.com.icesi.tallerjpa.enums.TypeAccount;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.strategy.accounts.interfaces.TypeAccountStrategy;
import org.springframework.stereotype.Component;

@Component
public class AccountDepositOnly implements TypeAccountStrategy {


    @Override
    public TypeAccount getType() {
        return TypeAccount.DEPOSIT_ONLY;
    }

    @Override
    public void withdraw(Long amount, Account account) {
        if (amount < 0) { throw new RuntimeException("The amount must be greater than 0"); }
        if(account.getBalance() < amount) { throw new RuntimeException("Insufficient funds"); }
        account.setBalance(account.getBalance() - amount);
    }

    @Override
    public void transfer(Long amount, Account accountOrigin, Account accountDestination, boolean isReceiverAccountValid) {
        throw new UnsupportedOperationException("This account type does not transfer");
    }

    @Override
    public boolean isReceiverAccountValid() {
        return false;
    }

    @Override
    public void deposit(Long amount, Account account) {
        if (amount < 0) { throw new RuntimeException("The amount must be greater than 0"); }
        account.setBalance(account.getBalance() + amount);
    }

}
