package co.com.icesi.TallerJPA.dto.requestDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiTransactionDTO {
    private String accountNumberOrigin;
    private String accountNumberDestination;
    private Long amount;
    private String messageResult;
}
