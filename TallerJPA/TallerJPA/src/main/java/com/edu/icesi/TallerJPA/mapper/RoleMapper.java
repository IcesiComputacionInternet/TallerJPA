package com.edu.icesi.TallerJPA.mapper;

import com.edu.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromIcesiRoleDTO(IcesiRoleDTO roleCreateDTO);

    IcesiRoleDTO fromIcesiRole(IcesiRole icesiRole);
}
