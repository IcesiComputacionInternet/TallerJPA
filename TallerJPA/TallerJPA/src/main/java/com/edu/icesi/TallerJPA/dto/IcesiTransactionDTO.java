package com.edu.icesi.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiTransactionDTO {

    private String sourceAccount;

    private String destinationAccount;

    private Long amountMoney;

    private String result;


    private Long finalBalanceSourceAccount;


    private Long finalBalanceDestinationAccount;
}