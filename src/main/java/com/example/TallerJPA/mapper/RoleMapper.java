package com.example.TallerJPA.mapper;

import com.example.TallerJPA.dto.RoleCreateDTO;
import com.example.TallerJPA.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    IcesiRole fromIcesiRoleDTO(RoleCreateDTO icesiRole);
    RoleCreateDTO fromIcesiRole(IcesiRole icesiRole);
}
