package co.com.icesi.demojpa.dto;

import lombok.Data;

@Data
public class TransactionOperationDTO {

    private String accountFrom;
    private String accountTo;
    private Long amount;

}