package icesi.university.accountSystem.dto;

import icesi.university.accountSystem.enums.TypeAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAccountDTO {
    private String accountNumber;
    private Long balance;
    private TypeAccount type;
    private boolean active;
    private ResponseUserDTO user;
}
