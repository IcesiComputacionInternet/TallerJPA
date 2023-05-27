package com.edu.icesi.demojpa.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
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
