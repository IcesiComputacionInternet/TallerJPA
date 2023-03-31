package co.com.icesi.TallerJPA.dto.response;

import co.com.icesi.TallerJPA.Enum.AccountType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class AccountResponseDTO {
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private AccountType type;
    private boolean active;
    private UserResponseDTO user;

}
