package com.edu.icesi.TallerJPA.dto;

import com.edu.icesi.TallerJPA.model.IcesiUser;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleCreateDTO {

    private String description;

    private String name;

    private List<IcesiUser> icesiUsers;
}
