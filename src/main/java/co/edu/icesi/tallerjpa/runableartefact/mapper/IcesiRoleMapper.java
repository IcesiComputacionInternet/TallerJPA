package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface IcesiRoleMapper {

        IcesiRole toIcesiRole(IcesiRoleDTO icesiRoleDTO);

        IcesiRoleDTO toIcesiRoleDTO(IcesiRole icesiRole);
}
