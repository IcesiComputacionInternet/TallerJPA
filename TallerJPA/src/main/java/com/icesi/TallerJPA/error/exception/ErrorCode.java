package com.icesi.TallerJPA.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ERR_404("ERR_404", "%s not found"),
    ERR_500("ERR_500", "Oops, we ran into an error"),
    ERR_REQUIRED_FIELD("ERR_REQUIRED_FIELD", "The field %s is required"),
    ERR_DUPLICATED("ERR_DUPLICATED", "resource %s with field %s: %s, already exists");

    private final String code;
    private final String message;
}
