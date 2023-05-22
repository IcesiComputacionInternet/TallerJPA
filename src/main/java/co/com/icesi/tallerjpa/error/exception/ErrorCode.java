package co.com.icesi.tallerjpa.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ERR_404("ERR_404", "%s with %s: %s not found"),
    ERR_500("ERR_500", "Oops, we ran into an error"),
    ERR_400("ERR_400", "field %s"),
    ERR_REQUIRED_FIELD("ERR_REQUIRED_FIELD", "field %s is required"),
    ERR_DUPLICATED("ERR_DUPLICATED", "resource %s already exists"),
    ERR_401("ERR_401", "Unauthorized response"),
    ERR_403("ERR_403", "Forbidden");


    private final String code;
    private final String message;

}
