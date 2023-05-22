package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.IcesiRoleResponseDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-22T08:18:24-0500",
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

    @Override
    public IcesiRoleDTO toIcesiRoleDTO(IcesiRoleResponseDTO icesiRoleResponseDTO) {
        if ( icesiRoleResponseDTO == null ) {
            return null;
        }

        IcesiRoleDTO.IcesiRoleDTOBuilder icesiRoleDTO = IcesiRoleDTO.builder();

        return icesiRoleDTO.build();
    }

    @Override
    public IcesiRoleResponseDTO toIcesiRoleResponseDTO(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        IcesiRoleResponseDTO icesiRoleResponseDTO = new IcesiRoleResponseDTO();

        return icesiRoleResponseDTO;
    }

    @Override
    public IcesiRoleResponseDTO toIcesiRoleResponseDTO(IcesiRoleDTO icesiRoleDTO) {
        if ( icesiRoleDTO == null ) {
            return null;
        }

        IcesiRoleResponseDTO icesiRoleResponseDTO = new IcesiRoleResponseDTO();

        return icesiRoleResponseDTO;
    }
}
