package icesi.university.accountSystem.strategy.accounts;

import icesi.university.accountSystem.enums.TypeAccount;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.strategy.interfaces.TypeAccountStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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
