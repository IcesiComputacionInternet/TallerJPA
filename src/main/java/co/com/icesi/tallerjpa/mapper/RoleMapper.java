package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.RoleDTO;
import co.com.icesi.tallerjpa.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role fromRoleDTO(RoleDTO roleDTO);
    RoleDTO fromRole(Role role);
}
