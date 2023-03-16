package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiRoleShowDTO;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    IcesiRole fromCreateIcesiRoleDTO(IcesiRoleCreateDTO icesiRoleCreateDTO);
    IcesiRoleShowDTO fromIcesiRoleToShowDTO(IcesiRole icesiRole);
}
