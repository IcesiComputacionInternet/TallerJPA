package co.com.icesi.demojpa.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountCreateDTO {
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private String type;
    private boolean active;
    private UserCreateDTO user;
}
