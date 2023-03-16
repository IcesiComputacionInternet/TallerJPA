package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.enums.TypeAccount;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ResponseAccountDTO {
    private UUID accountId;
    private String accountNumber;
    private Long balance;
    private TypeAccount type;
    private boolean active;
    private ResponseUserDTO user;
}
