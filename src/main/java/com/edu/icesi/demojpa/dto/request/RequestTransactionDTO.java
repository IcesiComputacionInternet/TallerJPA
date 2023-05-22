package com.edu.icesi.demojpa.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestTransactionDTO {
    private String accountFrom;
    private String accountTo;
    private long amount;
}
