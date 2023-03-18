package co.edu.icesi.tallerJPA.mapper;

import co.edu.icesi.tallerJPA.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerJPA.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    IcesiRole fromIcesiRoleCreateDTO(IcesiRoleCreateDTO icesiRoleCreateDTO);
}
