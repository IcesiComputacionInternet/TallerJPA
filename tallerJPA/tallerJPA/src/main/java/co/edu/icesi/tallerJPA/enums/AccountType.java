package co.edu.icesi.tallerJPA.enums;

import java.util.Arrays;

public enum AccountType {
    DEPOSIT_ONLY;

    public static  AccountType fromString(String text) {
        return Arrays.stream(AccountType.values())
                .filter(typeAccount -> typeAccount.name().compareToIgnoreCase(text) == 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account type NOT valid"));
    }
}
