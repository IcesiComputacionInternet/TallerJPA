package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-09T14:45:08-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class IcesiRoleMapperImpl implements IcesiRoleMapper {

    @Override
    public IcesiRole toIcesiRole(IcesiRoleDTO icesiRoleDTO) {
        if ( icesiRoleDTO == null ) {
            return null;
        }

        IcesiRole icesiRole = new IcesiRole();

        icesiRole.setName( icesiRoleDTO.getName() );
        icesiRole.setDescription( icesiRoleDTO.getDescription() );

        return icesiRole;
    }

    @Override
    public IcesiRoleDTO toIcesiRoleDTO(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        IcesiRoleDTO icesiRoleDTO = new IcesiRoleDTO();

        icesiRoleDTO.setName( icesiRole.getName() );
        icesiRoleDTO.setDescription( icesiRole.getDescription() );

        return icesiRoleDTO;
    }
}
