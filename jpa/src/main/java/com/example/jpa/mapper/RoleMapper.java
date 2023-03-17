package com.example.jpa.mapper;

import com.example.jpa.dto.RoleDTO;
import com.example.jpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromRoleDTO(RoleDTO roleDTO);
    RoleDTO fromRole(IcesiRole role);
}
