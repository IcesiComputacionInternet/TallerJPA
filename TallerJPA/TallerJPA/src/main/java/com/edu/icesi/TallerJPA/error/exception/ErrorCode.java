package com.edu.icesi.TallerJPA.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ERR_404("ERR_404", "%s WITH %s: %s NOT FOUND"),
    ERR_400("ERR_400", "%s FOR %sIS INVALID %s"),
    ERR_401("ERR_401", "UNAUTHORIZED"),
    ERR_500("ERR_500", "ERROR"),
    ERR_REQUIRED_FIELD("ERR_REQUIRED_FIELD", ""),
    ERR_DUPLICATED("ERR_DUPLICATED", "ALREADY EXISTS");
    private final String code;
    private final String message;

}
