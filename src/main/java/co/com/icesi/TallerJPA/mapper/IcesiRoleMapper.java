package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    IcesiRole fromIcesiRoleDTO(IcesiRoleCreateDTO icesiUserRoleDTO);
    IcesiRoleCreateDTO fromIcesiRole(IcesiRole icesiUserRole);
}