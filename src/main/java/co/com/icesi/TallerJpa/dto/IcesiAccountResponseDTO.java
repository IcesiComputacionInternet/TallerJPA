package co.com.icesi.TallerJpa.dto;

import co.com.icesi.TallerJpa.enums.AccountType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountResponseDTO {
    private String accountNumber;
    private Long balance;
    private AccountType type;
    private boolean active;
    private IcesiUserResponseDTO user;
}
