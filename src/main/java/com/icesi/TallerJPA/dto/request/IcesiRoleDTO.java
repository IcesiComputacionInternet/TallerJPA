package com.icesi.TallerJPA.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiRoleDTO {

    @NotEmpty
    private String description;
    @NotEmpty
    private String name;
}
