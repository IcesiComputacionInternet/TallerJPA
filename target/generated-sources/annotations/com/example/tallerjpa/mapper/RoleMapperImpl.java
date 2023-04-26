package com.example.tallerjpa.mapper;

import com.example.tallerjpa.dto.RoleDTO;
import com.example.tallerjpa.model.IcesiRole;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-20T15:37:02-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public IcesiRole fromRoleDTO(RoleDTO roleDTO) {
        if ( roleDTO == null ) {
            return null;
        }

        IcesiRole.IcesiRoleBuilder icesiRole = IcesiRole.builder();

        icesiRole.description( roleDTO.getDescription() );
        icesiRole.name( roleDTO.getName() );

        return icesiRole.build();
    }

    @Override
    public RoleDTO fromIcesiRole(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        RoleDTO.RoleDTOBuilder roleDTO = RoleDTO.builder();

        roleDTO.description( icesiRole.getDescription() );
        roleDTO.name( icesiRole.getName() );

        return roleDTO.build();
    }
}
