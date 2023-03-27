package com.example.jpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionRequestDTO {

    private String sourceAccount;
    private String targetAccount;
    private Long amount;
}
