package co.edu.icesi.demo.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ERR_404("ERR_404", "%s %s not found"),
    ERR_500("ERR_500", "Oops, we ran into an error"),
    ERR_400("ERR_400", "%s %s"),
    ERR_REQUIRED_FIELD("ERR_REQUIRED_FIELD", "field %s is required"),
    ERR_DUPLICATED("ERR_DUPLICATED", "%s %s already exists"),
    ERR_LOGIN("ERR_LOGIN","%s");
    private final String code;
    private final String message;

}
