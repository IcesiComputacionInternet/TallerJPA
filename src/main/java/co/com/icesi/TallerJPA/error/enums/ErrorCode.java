package co.com.icesi.TallerJPA.error.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ERR_403("ERR_403", "%s and %s already exists"),
    ERR_406("ERR_406", "%s already exists"),
    ERR_500("ERR_500", "Oops, we ran into an error"),
    ERR_500_ACC("ERR_500", "Oops, we ran into an error because %s"),
    ERR_400("ERR_400", "field %s %s"),
    ERR_REQUIRED_FIELD("ERR_REQUIRED_FIELD", "field %s is required"),
    ERR_NOT_EXITS("ERR_NOT_EXIST","The %s not exists"),
    ERR_NOT_FOUND("ERR_NOT_FOUND","%s not found"),
    ERR_DUPLICATED("ERR_DUPLICATED", "resource %s with field %s: %s, already exists");
    private final String code;
    private final String message;

}
