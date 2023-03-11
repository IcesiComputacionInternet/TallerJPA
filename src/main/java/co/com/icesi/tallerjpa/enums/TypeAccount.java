package co.com.icesi.tallerjpa.enums;

import java.util.Arrays;

public enum TypeAccount {
    DEPOSIT_ONLY;

    public static TypeAccount fromString(String text) {
        return Arrays.stream(TypeAccount.values())
                .filter(typeAccount -> typeAccount.name().compareToIgnoreCase(text) == 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid type account"));
    }
}
