package co.edu.icesi.demo.enums;

import co.edu.icesi.demo.strategy.AccountDepositOnly;
import co.edu.icesi.demo.strategy.AccountNormal;
import co.edu.icesi.demo.strategy.interfaces.TypeAccountStrategy;

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
