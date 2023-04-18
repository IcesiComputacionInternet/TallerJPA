package com.example.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
public class TransactionAccountDTO {
    private String accountNumber;
    @Min(value = 0)
    private long amount;
}
