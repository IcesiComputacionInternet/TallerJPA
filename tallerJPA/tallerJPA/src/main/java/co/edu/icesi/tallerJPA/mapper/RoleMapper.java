package co.edu.icesi.tallerJPA.mapper;

import co.edu.icesi.tallerJPA.dto.RoleDTO;
import co.edu.icesi.tallerJPA.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role fromRoleDTO(RoleDTO roleDTO);
}
