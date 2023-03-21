package co.com.icesi.tallerjpa.dto;

import lombok.Data;

@Data
public class TransactionOperationDTO {

    private String accountFrom;
    private String accountTo;
    private Long amount;

}
