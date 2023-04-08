package co.com.icesi.demojpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionOperationDTO {

    private String accountFrom;
    private String accountTo;
    private Long amount;

}