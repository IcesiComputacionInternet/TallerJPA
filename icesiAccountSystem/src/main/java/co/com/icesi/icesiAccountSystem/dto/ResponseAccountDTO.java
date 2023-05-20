package co.com.icesi.icesiAccountSystem.dto;

import co.com.icesi.icesiAccountSystem.enums.AccountType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAccountDTO {
    private ResponseUserDTO user;
    private String accountNumber;
    private long balance;
    private AccountType type;
    private boolean active;
}
