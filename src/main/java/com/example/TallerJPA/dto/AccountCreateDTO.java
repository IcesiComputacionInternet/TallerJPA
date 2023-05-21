package com.example.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AccountCreateDTO {
    @NotBlank
    private String userEmail;
    @NotNull
    private String type;
}
