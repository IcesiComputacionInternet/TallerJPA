package com.icesi.TallerJPA.dto.request;

import com.icesi.TallerJPA.enums.IcesiAccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
