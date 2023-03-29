package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-28T15:17:27-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class IcesiRoleMapperImpl implements IcesiRoleMapper {

    @Override
    public IcesiRole toIcesiRole(IcesiRoleDTO icesiRoleDTO) {
        if ( icesiRoleDTO == null ) {
            return null;
        }

        IcesiRole.IcesiRoleBuilder icesiRole = IcesiRole.builder();

        icesiRole.name( icesiRoleDTO.getName() );
        icesiRole.description( icesiRoleDTO.getDescription() );

        return icesiRole.build();
    }

    @Override
    public IcesiRoleDTO toIcesiRoleDTO(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        IcesiRoleDTO.IcesiRoleDTOBuilder icesiRoleDTO = IcesiRoleDTO.builder();

        icesiRoleDTO.name( icesiRole.getName() );
        icesiRoleDTO.description( icesiRole.getDescription() );

        return icesiRoleDTO.build();
    }
}
