package com.example.demo.mapper;

import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.DTO.ResponseIcesiRoleDTO;
import com.example.demo.model.IcesiRole;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-28T16:54:41-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230413-0857, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class IcesiRoleMapperImpl implements IcesiRoleMapper {

    @Override
    public IcesiRole fromIcesiRoleCreateDTO(IcesiRoleCreateDTO icesiRoleCreateDTO) {
        if ( icesiRoleCreateDTO == null ) {
            return null;
        }

        IcesiRole.IcesiRoleBuilder icesiRole = IcesiRole.builder();

        icesiRole.description( icesiRoleCreateDTO.getDescription() );
        icesiRole.name( icesiRoleCreateDTO.getName() );

        return icesiRole.build();
    }

    @Override
    public ResponseIcesiRoleDTO fromIcesiRoleToResponseIcesiRoleDTO(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        ResponseIcesiRoleDTO.ResponseIcesiRoleDTOBuilder responseIcesiRoleDTO = ResponseIcesiRoleDTO.builder();

        responseIcesiRoleDTO.description( icesiRole.getDescription() );
        responseIcesiRoleDTO.name( icesiRole.getName() );

        return responseIcesiRoleDTO.build();
    }
}
