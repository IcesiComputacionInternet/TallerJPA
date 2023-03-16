package com.edu.icesi.demojpa.mapper;

import com.edu.icesi.demojpa.dto.RoleCreateDTO;
import com.edu.icesi.demojpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    IcesiRole fromIcesiRoleDTO(RoleCreateDTO roleCreateDTO);
}
