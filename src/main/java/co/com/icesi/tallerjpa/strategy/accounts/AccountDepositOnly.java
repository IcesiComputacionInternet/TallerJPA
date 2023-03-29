package co.com.icesi.tallerjpa.strategy.accounts;

import co.com.icesi.tallerjpa.enums.TypeAccount;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.strategy.accounts.interfaces.TypeAccountStrategy;


public class AccountDepositOnly implements TypeAccountStrategy {
    @Override
    public TypeAccount getType() {
        return TypeAccount.DEPOSIT_ONLY;
    }

    @Override
    public void withdraw(Long amount, Account account) {
        generalValidations(account);
        if(account.getBalance() < amount) { throw new RuntimeException("Insufficient funds"); }
        account.setBalance(account.getBalance() - amount);
    }

    @Override
    public void transfer(Long amount, Account accountOrigin, Account accountDestination) {
        throw new UnsupportedOperationException("This account type does not transfer");
    }

    @Override
    public boolean isReceiverAccountValid() {
        return false;
    }

    @Override
    public void deposit(Long amount, Account account) {
        generalValidations(account);
        account.setBalance(account.getBalance() + amount);
    }

    private void generalValidations(Account account){
        if (!account.isActive()) {
            throw new RuntimeException("The account " + account.getAccountNumber() + " is not active");
        }
    }

}
