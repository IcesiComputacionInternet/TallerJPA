package co.com.icesi.tallerjpa.enums;

import co.com.icesi.tallerjpa.strategy.accounts.AccountDepositOnly;
import co.com.icesi.tallerjpa.strategy.accounts.AccountNormal;
import co.com.icesi.tallerjpa.strategy.accounts.interfaces.TypeAccountStrategy;

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
