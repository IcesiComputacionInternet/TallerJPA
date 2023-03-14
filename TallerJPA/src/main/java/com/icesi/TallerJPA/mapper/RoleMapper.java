package com.icesi.TallerJPA.mapper;

import com.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.icesi.TallerJPA.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromIcesiRoleDTO(IcesiRoleDTO role);

    IcesiRoleDTO fromIcesiRole(IcesiRole role);
}
