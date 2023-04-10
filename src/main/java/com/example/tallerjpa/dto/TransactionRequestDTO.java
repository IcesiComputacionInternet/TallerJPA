package com.example.tallerjpa.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionRequestDTO {
    private String originAccountNumber;
    private String destinationAccountNumber;
    private long amount;
}
