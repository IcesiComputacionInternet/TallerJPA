package com.edu.icesi.demojpa.error.exception;

import lombok.Getter;

@Getter
public record DetailBuilder(ErrorCode errorCode, Object... fields) { }
