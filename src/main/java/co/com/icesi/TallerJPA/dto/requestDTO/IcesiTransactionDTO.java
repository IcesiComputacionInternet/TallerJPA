package co.com.icesi.TallerJPA.dto.requestDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IcesiTransactionDTO {
    private String accountNumberOrigin;
    private String accountNumberDestination;
    private Long amount;
    private String messageResult;
}
