package com.example.tallerjpa.mapper;

import com.example.tallerjpa.dto.RoleDTO;
import com.example.tallerjpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromRoleDTO (RoleDTO roleDTO);
    RoleDTO fromIcesiRole (IcesiRole icesiRole);

}
