package com.example.jpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponseDTO {

    private String accountNumber;
    private Long balance;
    private String type;
    private boolean active;
    private UserResponseDTO user;
}
