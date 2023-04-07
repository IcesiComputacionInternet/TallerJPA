package com.example.demo.mapper;

import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.model.IcesiRole;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-07T13:50:07-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20230218-1114, environment: Java 17.0.6 (Eclipse Adoptium)"
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
    public IcesiRoleCreateDTO fromIcesiRole(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        IcesiRoleCreateDTO.IcesiRoleCreateDTOBuilder icesiRoleCreateDTO = IcesiRoleCreateDTO.builder();

        icesiRoleCreateDTO.description( icesiRole.getDescription() );
        icesiRoleCreateDTO.name( icesiRole.getName() );

        return icesiRoleCreateDTO.build();
    }
}
