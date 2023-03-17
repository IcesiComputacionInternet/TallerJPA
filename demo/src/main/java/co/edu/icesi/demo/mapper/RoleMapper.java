package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.RoleCreateDTO;
import co.edu.icesi.demo.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromIcesiRoleDTO(RoleCreateDTO roleCreateDTO);

}
