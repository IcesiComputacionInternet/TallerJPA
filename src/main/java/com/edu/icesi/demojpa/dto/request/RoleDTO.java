package com.edu.icesi.demojpa.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    @NotBlank(message = "The field can't be blank")
    @NotNull(message = "The field can't be null")
    private String description;
    @NotBlank(message = "The field can't be blank")
    @NotNull(message = "The field can't be null")
    private String name;
}
