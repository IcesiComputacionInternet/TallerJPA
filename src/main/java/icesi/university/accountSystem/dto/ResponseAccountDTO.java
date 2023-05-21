package icesi.university.accountSystem.dto;

import icesi.university.accountSystem.enums.TypeAccount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAccountDTO {
    private String accountNumber;
    private Long balance;
    private TypeAccount type;
    private boolean active;
    private ResponseUserDTO user;
}
