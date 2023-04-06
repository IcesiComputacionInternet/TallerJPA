package com.edu.icesi.demojpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseTransactionDTO {
    private String result;
    private String accountFrom;
    private String accountTo;
    private long amount;
}
