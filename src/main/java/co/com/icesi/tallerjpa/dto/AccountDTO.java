package co.com.icesi.tallerjpa.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountDTO {
    private UUID accountId;
    private String accountNumber;
    private Long balance;
    private String type;
    private boolean active;
}
