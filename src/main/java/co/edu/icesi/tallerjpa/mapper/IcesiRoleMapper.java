package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {

    IcesiRole toIcesiRole(IcesiRoleDTO icesiRoleDTO);
    IcesiRoleDTO toIcesiRoleDTO(IcesiRole icesiRole);

}
