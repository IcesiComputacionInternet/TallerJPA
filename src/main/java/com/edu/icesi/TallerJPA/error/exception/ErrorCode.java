package com.edu.icesi.TallerJPA.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ERR_404("ERR_404", "%s with %s: %s not found"),
    ERR_400("ERR_400", "%s for %s is invalid. %s"),
    ERR_401("ERR_401", "Unauthorized. Please check your credentials"),
    ERR_500("ERR_500", "Oops, we ran into an error"),
    ERR_REQUIRED_FIELD("ERR_REQUIRED_FIELD", "field %s is required"),
    ERR_DUPLICATED("ERR_DUPLICATED", "the %s with field %s: %s, already exists");
    private final String code;
    private final String message;

}
