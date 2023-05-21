package com.example.demo.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseTransactionDTO {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private long balance;  
    private String result;
}
