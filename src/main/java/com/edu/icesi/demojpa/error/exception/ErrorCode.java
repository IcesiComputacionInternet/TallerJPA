package com.edu.icesi.demojpa.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ERROR_404("ERR_404", "% with %s: %s not found"),
    ERROR_500("ERR_500", "Oops, we ran into an error"),
    ERROR_400("ERR_400", "Fields %s %s"),
    ERR_REQUIRED_FIELD("ERR_REQUIRED_FIELD", "Fields %s is required"),
    ERR_DUPLICATED("ERR_DUPLICATED", "Resource %s with field %s: %s, already exists");

    private final String code;
    private final String message;
}
