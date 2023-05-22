package co.com.icesi.demojpa.mapper;

import co.com.icesi.demojpa.dto.request.RoleCreateDTO;
import co.com.icesi.demojpa.dto.response.RoleResponseDTO;
import co.com.icesi.demojpa.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    IcesiRole fromRoleCreateDTO(RoleCreateDTO roleCreateDTO);

    RoleResponseDTO fromIcesiRole(IcesiRole icesiRole);
}
