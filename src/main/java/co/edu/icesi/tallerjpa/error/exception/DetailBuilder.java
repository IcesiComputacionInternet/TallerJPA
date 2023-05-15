package co.edu.icesi.tallerjpa.error.exception;

import lombok.Getter;

@Getter
public record DetailBuilder(ErrorCode errorCode, Object... fields) {
}
