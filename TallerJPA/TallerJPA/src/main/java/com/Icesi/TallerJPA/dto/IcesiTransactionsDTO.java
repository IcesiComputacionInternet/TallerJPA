package com.Icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiTransactionsDTO {


    private String accountOrigin;
    private String accountDestination;
    private Long amount;

    private String result;
}
