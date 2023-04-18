package com.example.TallerJPA.error.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ErrorDetail {
    private String errorCode;
    private String errorMessage;
}
