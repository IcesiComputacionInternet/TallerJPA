package co.com.icesi.TallerJpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTO {
    private String accountNumberDeparture;
    private String accountNumberDestiny;
    private Long amount;
}
