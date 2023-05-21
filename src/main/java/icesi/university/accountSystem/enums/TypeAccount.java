package icesi.university.accountSystem.enums;

import icesi.university.accountSystem.strategy.accounts.AccountDepositOnly;
import icesi.university.accountSystem.strategy.accounts.AccountNormal;
import icesi.university.accountSystem.strategy.interfaces.TypeAccountStrategy;

public enum TypeAccount {
    DEPOSIT_ONLY(new AccountDepositOnly()), ACCOUNT_NORMAL(new AccountNormal());
    private final TypeAccountStrategy strategy;

    TypeAccount(TypeAccountStrategy strategy) {
        this.strategy = strategy;
    }

    public TypeAccountStrategy getStrategy() {
        return strategy;
    }
}
