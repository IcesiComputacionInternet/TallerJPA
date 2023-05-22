package co.com.icesi.demojpa.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.management.loading.MLetContent;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ERR_404("ERR_404", "%s not found"),
    ERR_500("ERR_500", "Oops, we ran into an error"),
    ERR_UNAUTHORIZED("ERR_UNAUTHORIZED", "The user is not authorized"),
    ERR_METHOD_ARGUMENT_NOT_VALID("ERR_METHOD_ARGUMENT_NOT_VALID", "%s"),
    ERR_REQUIRED_FIELD("ERR_REQUIRED_FIELD", "The field %s is required"),
    ERR_DUPLICATED("ERR_DUPLICATED", "resource %s with field %s: %s, already exists"),
    ERR_DONT_VALUE("ERR_DONT_VALUE", "The field %s don't have a valid value"),
    ERR_ILLEGAL("ERR_ILLEGAL", "The field %s don't have a valid value"),
    ERR_DONT_DISABLE("ERR_DONT_DISABLE", "The field %s don't have a valid value"),
    ERR_ACCOUNT_INACTIVE("ERR_ACCOUNT_INACTIVE", "The account is inactive"),
    ERR_UNAUTHORIZED_CREATE_USER("ERR_UNAUTHORIZED_CREATE_USER", "The user is not authorized");

    private final String code;
    private final String message;
}
