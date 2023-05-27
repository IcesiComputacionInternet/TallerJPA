package com.edu.icesi.demojpa.error.exception;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class IcesiErrorDetail {
    private String errorCode;
    private String errorMessage;
}
