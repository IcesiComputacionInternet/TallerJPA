package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResultDTO {
    private String senderAccountId;
    private String receiverAccountId;
    private long balance;
    private String result;
}
