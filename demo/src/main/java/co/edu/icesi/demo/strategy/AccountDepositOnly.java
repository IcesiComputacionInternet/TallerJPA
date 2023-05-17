package co.edu.icesi.demo.strategy;

import co.edu.icesi.demo.enums.TypeAccount;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.strategy.interfaces.TypeAccountStrategy;

public class AccountDepositOnly implements TypeAccountStrategy {


    @Override
    public TypeAccount getType() {
        return TypeAccount.DEPOSIT_ONLY;
    }

    @Override
    public void withdraw(Long amount, IcesiAccount account) {
        generalValidations(account, amount);
        if(account.getBalance() < amount) { throw new RuntimeException("Insufficient funds"); }
        account.setBalance(account.getBalance() - amount);
    }

    @Override
    public void transfer(Long amount, IcesiAccount accountOrigin, IcesiAccount accountDestination, boolean isReceiverAccountValid) {
        throw new UnsupportedOperationException("This account type does not transfer");
    }

    @Override
    public boolean isReceiverAccountValid() {
        return false;
    }

    @Override
    public void deposit(Long amount, IcesiAccount account) {
        generalValidations(account, amount);
        account.setBalance(account.getBalance() + amount);
    }

    private void generalValidations(IcesiAccount account, Long amount){
        if (!account.isActive()) {
            throw new RuntimeException("The account " + account.getAccountNumber() + " is not active");
        }
        if (amount < 0) { throw new RuntimeException("The amount must be greater than 0"); }
    }

}
