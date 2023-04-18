package com.icesi.TallerJPA.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiRoleDTO {

    @NotBlank
    private String description;
    @NotBlank
    private String name;
}
