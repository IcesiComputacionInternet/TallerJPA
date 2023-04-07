package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiTransactionDTO {
    private String accountNumberOrigin;
    private String accountNumberDestination;
    private Long amount;
    private String messageResult;
}
