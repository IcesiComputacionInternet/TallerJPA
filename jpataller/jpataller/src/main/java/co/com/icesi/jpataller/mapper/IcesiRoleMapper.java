package co.com.icesi.jpataller.mapper;

import co.com.icesi.jpataller.dto.IcesiRoleDTO;
import co.com.icesi.jpataller.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {

    IcesiRole fromDTO(IcesiRoleDTO icesiRoleDTO);

    IcesiRoleDTO fromModel(IcesiRole icesiRole);
}
