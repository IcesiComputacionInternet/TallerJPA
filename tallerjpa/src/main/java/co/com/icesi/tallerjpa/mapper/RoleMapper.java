package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import co.com.icesi.tallerjpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    IcesiRole fromIcesiRoleDTO(RoleCreateDTO roleCreateDTO);
    RoleCreateDTO fromIcesiRole(IcesiRole icesiRole);
}
