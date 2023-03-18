package co.com.icesi.TallerJpa.mapper;

import co.com.icesi.TallerJpa.dto.IcesiRoleCreateDTO;
import co.com.icesi.TallerJpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    IcesiRole fromRoleDto(IcesiRoleCreateDTO icesiRoleCreateDTO);
    IcesiRoleCreateDTO fromIcesiRole(IcesiRole icesiRole);
}
