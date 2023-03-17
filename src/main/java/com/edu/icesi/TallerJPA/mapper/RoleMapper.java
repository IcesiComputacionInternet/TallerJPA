package com.edu.icesi.TallerJPA.mapper;

import com.edu.icesi.TallerJPA.dto.RoleCreateDTO;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromIcesiRoleDTO(RoleCreateDTO roleCreateDTO);
}
