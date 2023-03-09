package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromIcesiRoleDTO(RoleCreateDTO roleCreateDTO);
    RoleCreateDTO fromICesiRole(IcesiRole icesiRole);
}
