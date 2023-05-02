package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.Enum.AccountType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@Builder
public class ResponseAccountDTO {
    private String accountNumber;
    private Long balance;
    private AccountType type;
    private boolean active;
    private ResponseUserDTO user;
}