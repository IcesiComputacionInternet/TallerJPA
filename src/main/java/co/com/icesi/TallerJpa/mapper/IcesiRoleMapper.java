package co.com.icesi.TallerJpa.mapper;

import co.com.icesi.TallerJpa.dto.IcesiRoleDTO;
import co.com.icesi.TallerJpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    IcesiRole fromRoleDto(IcesiRoleDTO icesiRoleDTO);
    IcesiRoleDTO fromIcesiRole(IcesiRole icesiRole);
}
