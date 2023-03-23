package com.example.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountResponseDTO {
    private String accountNumber;
    private String userEmail;
    private long balance;
    private boolean active;
}
