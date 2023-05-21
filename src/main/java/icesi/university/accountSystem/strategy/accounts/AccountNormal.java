package icesi.university.accountSystem.strategy.accounts;

import icesi.university.accountSystem.enums.TypeAccount;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.strategy.interfaces.TypeAccountStrategy;

public class AccountNormal implements TypeAccountStrategy {

    @Override
    public TypeAccount getType() {
        return TypeAccount.ACCOUNT_NORMAL;
    }

    @Override
    public void withdraw(Long amount, IcesiAccount account) {
        generalValidations(account, amount);
        if(account.getBalance() < amount) { throw new RuntimeException("Insufficient funds"); }
        account.setBalance(account.getBalance() - amount);

    }

    @Override
    public void transfer(Long amount, IcesiAccount accountOrigin, IcesiAccount accountDestination, boolean isReceiverAccountValid) {
        generalValidations(accountOrigin, amount);
        generalValidations(accountDestination, amount);
        if(!isReceiverAccountValid) { throw new RuntimeException("The account type does not allow transfers"); }
        if(accountOrigin.getBalance() < amount) { throw new RuntimeException("Insufficient funds"); }
        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountDestination.setBalance(accountDestination.getBalance() + amount);
    }

    @Override
    public boolean isReceiverAccountValid() {
        return true;
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
