package com.edu.icesi.demojpa.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestTransactionDTO {
    private String accountFrom;
    private String accountTo;
    private long amount;
}
