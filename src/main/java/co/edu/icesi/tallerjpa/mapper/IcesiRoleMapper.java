package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.CreateIcesiRoleDTO;
import co.edu.icesi.tallerjpa.dto.ShowIcesiRoleDTO;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    IcesiRole fromCreateIcesiRoleDTO(CreateIcesiRoleDTO createIcesiRoleDTO);
    ShowIcesiRoleDTO fromIcesiRoleToShowDTO(IcesiRole icesiRole);
}
