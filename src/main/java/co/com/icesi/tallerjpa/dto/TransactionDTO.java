package co.com.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTO {

    private String accountFrom;
    private String accountTo;
    private Long amount;
    private String result;
}
