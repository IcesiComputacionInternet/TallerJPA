package com.edu.icesi.TallerJPA.dto;

import lombok.Data;

@Data
public class TransactionDTO {

    private String sourceAccount;

    private String destinationAccount;

    private Long amountMoney;

    private String result;

}
