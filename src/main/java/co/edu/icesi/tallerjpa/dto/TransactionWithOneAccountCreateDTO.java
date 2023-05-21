package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionWithOneAccountCreateDTO {
    private String accountNumber;
    private long amount;
}
