package com.edu.icesi.TallerJPA.dto;

import com.edu.icesi.TallerJPA.model.IcesiUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class IcesiAccountDTO {


    private String accountNumber;


    private long balance;


    private String type;


    private boolean active;

    @NotNull()
    @NotBlank
    private IcesiUser icesiUser;
}
