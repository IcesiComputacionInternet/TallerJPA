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
public class AccountCreateDTO {

    @NotNull(message = "Account must have an account number")
    private String accountNumber;

    @Min(value = 0, message = "Balance should be greater than zero")
    @Max(value = 1000000)
    private long balance;

    @NotNull(message = "Account must be of a type")
    private String type;

    @NotNull(message = "Account must have a status")
    private boolean active;

    @NotNull(message = "Account must have an icesi user")
    @NotBlank
    private IcesiUser icesiUser;
}
