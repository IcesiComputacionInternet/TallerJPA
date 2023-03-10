package co.com.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountDTO {
    private UUID accountId;
    private String accountNumber;
    private Long balance;
    private String type;
    private boolean active;
}
