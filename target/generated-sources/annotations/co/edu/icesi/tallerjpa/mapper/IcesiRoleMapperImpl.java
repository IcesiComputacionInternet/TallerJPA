package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-28T13:14:02-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230413-0857, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class IcesiRoleMapperImpl implements IcesiRoleMapper {

    @Override
    public IcesiRole toIcesiRole(IcesiRoleDTO icesiRoleDTO) {
        if ( icesiRoleDTO == null ) {
            return null;
        }

        IcesiRole.IcesiRoleBuilder icesiRole = IcesiRole.builder();

        icesiRole.description( icesiRoleDTO.getDescription() );
        icesiRole.name( icesiRoleDTO.getName() );

        return icesiRole.build();
    }

    @Override
    public IcesiRoleDTO toIcesiRoleDTO(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        IcesiRoleDTO.IcesiRoleDTOBuilder icesiRoleDTO = IcesiRoleDTO.builder();

        icesiRoleDTO.description( icesiRole.getDescription() );
        icesiRoleDTO.name( icesiRole.getName() );

        return icesiRoleDTO.build();
    }
}
