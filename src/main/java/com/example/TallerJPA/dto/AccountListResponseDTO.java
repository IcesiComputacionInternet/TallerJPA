package com.example.TallerJPA.dto;

import com.example.TallerJPA.model.IcesiAccount;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AccountListResponseDTO {
    private List<AccountResponseDTO> accounts;
}
