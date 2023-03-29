package co.com.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTO {

    private String accountNumberOrigin;
    private String accountNumberDestination;
    private Long amount;
    private String message;
}
