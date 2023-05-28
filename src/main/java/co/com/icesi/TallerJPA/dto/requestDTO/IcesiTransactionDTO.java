package co.com.icesi.TallerJPA.dto.requestDTO;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiTransactionDTO {
    @NotBlank
    private String accountNumberOrigin;
    @NotBlank
    private String accountNumberDestination;
    @NotBlank
    private Long amount;
    private String messageResult;
}
