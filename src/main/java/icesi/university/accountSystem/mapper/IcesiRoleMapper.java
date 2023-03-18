package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.IcesiRoleDTO;
import icesi.university.accountSystem.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    IcesiRole fromIcesiRoleDTO(IcesiRoleDTO icesiRoleDTO);

    IcesiRoleDTO fromIcesiRole(IcesiRole icesiRole);
}
