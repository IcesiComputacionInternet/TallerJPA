package com.example.demo.DTO;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class IcesiAccountCreateDTO {

    @NotBlank
    private String userEmail;
    @NotNull
    private String type;
}
