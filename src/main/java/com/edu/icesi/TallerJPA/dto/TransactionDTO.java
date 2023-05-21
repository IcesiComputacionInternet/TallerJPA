package com.edu.icesi.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class TransactionDTO {

    @NotBlank(message = "Is necessary to have a source account for the transaction")
    @NotNull(message = "Is necessary to have a source account for the transaction")
    private String sourceAccount;

    private String destinationAccount;

    @Min(value = 0, message = "Money should be greater than zero")
    @Max(value = 1000000)
    private Long amountMoney;

    private String result;

    @Min(value = 0, message = "Insufficient money")
    private Long finalBalanceSourceAccount;

    @Min(value = 0, message = "Insufficient money")
    private Long finalBalanceDestinationAccount;
}