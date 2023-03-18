package icesi.university.accountSystem.dto;

import icesi.university.accountSystem.model.IcesiUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccountDTO {
    private UUID accountId;
    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    private IcesiUser user;
}
