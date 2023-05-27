package com.example.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private String accountNumber;
    private String userEmail;
    private long balance;
    private boolean active;

    private String accountType;
}
