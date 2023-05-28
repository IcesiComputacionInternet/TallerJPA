package com.example.tallerjpa.dto;

import com.example.tallerjpa.enums.AccountType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponseDTO {
    private String accountNumber;
    private Long balance;
    private AccountType type;
    private boolean active;
}
