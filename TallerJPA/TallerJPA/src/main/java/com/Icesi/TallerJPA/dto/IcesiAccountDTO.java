package com.Icesi.TallerJPA.dto;

import com.Icesi.TallerJPA.model.IcesiUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class IcesiAccountDTO {
    private Long balance;
    private String accountNumber;
    private String type;
    private boolean active;
}
