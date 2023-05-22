package co.com.icesi.demojpa.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class TransactionDTO {
    public TransactionDTO() {
    }

    public TransactionDTO(String accountNumberOrigin, String accountNumberDestination, long amount, String resultMessage) {
        this.accountNumberOrigin = accountNumberOrigin;
        this.accountNumberDestination = accountNumberDestination;
        this.amount = amount;
        this.resultMessage=resultMessage;

    }
    private String accountNumberOrigin;
    private String accountNumberDestination;
    private long amount;

    private String resultMessage;
}
