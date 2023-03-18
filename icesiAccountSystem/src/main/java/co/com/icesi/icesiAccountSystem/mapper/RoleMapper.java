package co.com.icesi.icesiAccountSystem.mapper;

import co.com.icesi.icesiAccountSystem.dto.RoleDTO;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    IcesiRole fromRoleDTO(RoleDTO userDTO);
}
