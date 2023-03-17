package com.edu.icesi.TallerJPA.dto;

import com.edu.icesi.TallerJPA.model.IcesiRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreateDTO {

    private String accountNumber;

    private long balance;

    private String type;

    private boolean active;

    private IcesiRole role;
}
