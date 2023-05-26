package com.example.jpa.error.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class IcesiErrorDetail {

    private IcesiErrorCode errorCode;
    private String errorMessage;
}
