package com.example.tallerjpa.error.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomDetail {
    private String errorCode;
    private String errorMessage;
}