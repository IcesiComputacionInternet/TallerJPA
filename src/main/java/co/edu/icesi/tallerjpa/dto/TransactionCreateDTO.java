package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionCreateDTO {
    private String senderAccountId;
    private String receiverAccountId;
    private long amount;
}
