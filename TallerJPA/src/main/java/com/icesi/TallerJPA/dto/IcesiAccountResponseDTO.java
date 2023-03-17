package com.icesi.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiAccountResponseDTO {

    private UUID accountId;
    private String accountNumber;
    private long balance;
    private String type;
    private Boolean active;
    private IcesiUserResponseDTO icesiUser;
}

