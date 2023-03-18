package com.icesi.TallerJPA.dto;

import com.icesi.TallerJPA.enums.IcesiAccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiAccountDTO {

    private long balance;
    private IcesiAccountType type;
    private Boolean active;
    private String emailUser;
}
