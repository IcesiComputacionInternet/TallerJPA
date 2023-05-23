package com.icesi.TallerJPA.mapper;

import com.icesi.TallerJPA.dto.request.IcesiRoleDTO;
import com.icesi.TallerJPA.dto.response.IcesiRoleResponseDTO;
import com.icesi.TallerJPA.model.IcesiRole;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-23T14:57:20-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public IcesiRole fromIcesiRoleDTO(IcesiRoleDTO role) {
        if ( role == null ) {
            return null;
        }

        IcesiRole.IcesiRoleBuilder icesiRole = IcesiRole.builder();

        icesiRole.description( role.getDescription() );
        icesiRole.name( role.getName() );

        return icesiRole.build();
    }

    @Override
    public IcesiRoleResponseDTO toResponse(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        IcesiRoleResponseDTO.IcesiRoleResponseDTOBuilder icesiRoleResponseDTO = IcesiRoleResponseDTO.builder();

        icesiRoleResponseDTO.roleId( icesiRole.getRoleId() );
        icesiRoleResponseDTO.description( icesiRole.getDescription() );
        icesiRoleResponseDTO.name( icesiRole.getName() );

        return icesiRoleResponseDTO.build();
    }
}
