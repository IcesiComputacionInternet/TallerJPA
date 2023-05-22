package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.RoleDTO;
import co.edu.icesi.demo.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromIcesiRoleDTO(RoleDTO roleDTO);

    RoleDTO fromIcesiRole(IcesiRole icesiRole);

}
