package com.edu.icesi.TallerJPA.dto;

import com.edu.icesi.TallerJPA.model.IcesiUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateDTO {

    @NotNull(message = "Role need a description")
    private String description;

    @NotNull(message = "A name is required for the role")
    @Column(unique = true)
    private String name;

    private List<IcesiUser> icesiUsers;
}
