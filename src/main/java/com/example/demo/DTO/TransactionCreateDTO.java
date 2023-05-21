package com.example.demo.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionCreateDTO {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private long amount;//Amount to witrawal, deposit or transfer
}
