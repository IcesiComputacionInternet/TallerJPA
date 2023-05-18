package com.example.demo.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseTransactionDTO {
    private String senderAccountId;
    private String receiverAccountId;
    private long balance;  
    private String result;
}
