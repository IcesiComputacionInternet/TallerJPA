package com.edu.icesi.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDTO {

    private String sourceAccount;

    private String destinationAccount;

    private Long amountMoney;

    private String result;

    private Long finalBalanceSourceAccount;

    private Long finalBalanceDestinationAccount;
}
