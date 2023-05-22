package com.example.tallerjpa.error.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IcesiErrorDetail {

    private String errorCode;
    private String errorMessage;
}