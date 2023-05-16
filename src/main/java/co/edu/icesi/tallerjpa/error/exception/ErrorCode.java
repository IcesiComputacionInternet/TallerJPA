package co.edu.icesi.tallerjpa.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ERR_400("ERR_400", "field %s: %s"),
    ERR_404("ERR_404", "%s with %s: %s not found"),
    ERR_500("ERR_500", "%s");
    private final String code;
    private final String message;
}
