package co.com.icesi.demojpa.mapper;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromIcesiRoleDTO(RoleCreateDTO roleCreateDTO);

    RoleCreateDTO fromIcesiRole(IcesiRole icesiRole);
}
