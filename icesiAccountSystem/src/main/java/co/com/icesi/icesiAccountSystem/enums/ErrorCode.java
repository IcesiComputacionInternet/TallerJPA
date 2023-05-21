package co.com.icesi.icesiAccountSystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor

public enum ErrorCode {
    ERR_404("ERR_404", "%s with %s: %s not found."),
    ERR_500("ERR_500", "Oops, we ran into an error."),
    ERR_400("ERR_400", "Field %s %s."),
    ERR_DUPLICATED("ERR_DUPLICATED", "Resource %s with field %s: %s, already exists."),
    ERR_BALANCE_BELOW_ZERO("ERR_BALANCE_BELOW_ZERO", "Account's balance can not be below 0."),
    ERR_ACCOUNT_TYPE("ERR_ACCOUNT_TYPE", "Account's type has to be deposit only or normal."),
    ERR_501("ERR_501","Login failed"),
    ERR_DISABLE_ACCOUNT("ERR_DISABLE_ACCOUNT", "Account's actual balance is %s");
    private final String code;
    private final String message;

}
