package co.com.icesi.icesiAccountSystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {
    private String userEmail;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;
}
