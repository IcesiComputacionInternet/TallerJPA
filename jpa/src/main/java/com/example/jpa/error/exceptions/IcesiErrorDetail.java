package com.example.jpa.error.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class IcesiErrorDetail {

    private String errorCode;
    private String errorMessage;
}
