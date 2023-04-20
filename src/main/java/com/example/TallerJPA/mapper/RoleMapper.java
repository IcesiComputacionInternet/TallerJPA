package com.example.TallerJPA.mapper;

import com.example.TallerJPA.dto.RoleDTO;
import com.example.TallerJPA.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    IcesiRole fromIcesiRoleDTO(RoleDTO icesiRole);
    RoleDTO fromIcesiRole(IcesiRole icesiRole);
}
