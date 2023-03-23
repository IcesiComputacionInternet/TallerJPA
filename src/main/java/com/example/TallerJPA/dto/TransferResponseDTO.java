package com.example.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransferResponseDTO {
    private String originAccountNumber;
    private String destinationAccountNumber;
    private long amount;
    private long balance;

}
