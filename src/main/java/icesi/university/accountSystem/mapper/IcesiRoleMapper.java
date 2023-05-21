package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.IcesiRoleDTO;
import icesi.university.accountSystem.model.IcesiRole;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    IcesiRole fromIcesiRoleDTO(IcesiRoleDTO icesiRoleDTO);

    IcesiRoleDTO fromIcesiRole(IcesiRole icesiRole);

    List<IcesiRoleDTO> fromIcesiRoles (List<IcesiRole> allRoles);
}
