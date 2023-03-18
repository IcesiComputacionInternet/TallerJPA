package com.Icesi.TallerJPA.mapper;

import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {

    IcesiRoleDTO fromIcesiRole(IcesiRole icesiRole);
    IcesiRole fromIcesiRoleDTO(IcesiRoleDTO icesiRoleDTO);
}
