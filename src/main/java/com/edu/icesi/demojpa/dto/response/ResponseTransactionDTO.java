package com.edu.icesi.demojpa.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseTransactionDTO {
    private String result;
    private String accountFrom;
    private String accountTo;
    private long amount;
}
