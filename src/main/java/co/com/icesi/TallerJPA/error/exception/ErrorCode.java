package co.com.icesi.TallerJPA.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public  enum ErrorCode {
    ERROR_EMAIL("This email already exist"),
    ERROR_PHONE("This phone already exist "),
    ERROR_USER_NOT_FOUND("The user you're looking was not found"),
    ERROR_SCOPE("You can not access this route"),
    ERROR_NOT_BELONG("You can not edit or create an account that does not belong to you"),
    ERROR_ACCOUNT_NOT_FOUND("The user you're looking was not found"),
    ERROR_USER_TO_CREATE("You cant create a user with that role"),
    ERROR_LOGIN("Credentials for login are wrong"),
    ERROR_MISSING_ARGUMENT("Complete the required fields"),
    RUNTIME_ERROR("There is an error while running the app");
    private  final String message;

}
