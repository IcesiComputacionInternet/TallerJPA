package com.example.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionAccountDTO {
    private String accountNumber;
    private long amount;
}
