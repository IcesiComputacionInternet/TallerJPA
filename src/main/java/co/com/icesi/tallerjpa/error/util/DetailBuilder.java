package co.com.icesi.tallerjpa.error.util;

import co.com.icesi.tallerjpa.error.enums.ErrorCode;

public record DetailBuilder(ErrorCode errorCode, Object... fields) { }
