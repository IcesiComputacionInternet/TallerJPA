package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.IcesiUserResponseDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-18T15:03:20-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class IcesiUserMapperImpl implements IcesiUserMapper {

    @Override
    public IcesiUser toIcesiUser(IcesiUserDTO icesiUserDTO) {
        if ( icesiUserDTO == null ) {
            return null;
        }

        IcesiUser.IcesiUserBuilder icesiUser = IcesiUser.builder();

        icesiUser.firstName( icesiUserDTO.getFirstName() );
        icesiUser.lastName( icesiUserDTO.getLastName() );
        icesiUser.email( icesiUserDTO.getEmail() );
        icesiUser.phoneNumber( icesiUserDTO.getPhoneNumber() );
        icesiUser.password( icesiUserDTO.getPassword() );

        return icesiUser.build();
    }

    @Override
    public IcesiUserDTO toIcesiUserDTO(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        IcesiUserDTO.IcesiUserDTOBuilder icesiUserDTO = IcesiUserDTO.builder();

        icesiUserDTO.firstName( icesiUser.getFirstName() );
        icesiUserDTO.lastName( icesiUser.getLastName() );
        icesiUserDTO.email( icesiUser.getEmail() );
        icesiUserDTO.phoneNumber( icesiUser.getPhoneNumber() );
        icesiUserDTO.password( icesiUser.getPassword() );

        return icesiUserDTO.build();
    }

    @Override
    public IcesiUserDTO toIcesiUserDTO(IcesiUserResponseDTO icesiUserResponseDTO) {
        if ( icesiUserResponseDTO == null ) {
            return null;
        }

        IcesiUserDTO.IcesiUserDTOBuilder icesiUserDTO = IcesiUserDTO.builder();

        return icesiUserDTO.build();
    }

    @Override
    public IcesiUserResponseDTO toIcesiUserResponseDTO(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        IcesiUserResponseDTO icesiUserResponseDTO = new IcesiUserResponseDTO();

        return icesiUserResponseDTO;
    }

    @Override
    public IcesiUserResponseDTO toIcesiUserResponseDTO(IcesiUserDTO icesiUserDTO) {
        if ( icesiUserDTO == null ) {
            return null;
        }

        IcesiUserResponseDTO icesiUserResponseDTO = new IcesiUserResponseDTO();

        return icesiUserResponseDTO;
    }
}
