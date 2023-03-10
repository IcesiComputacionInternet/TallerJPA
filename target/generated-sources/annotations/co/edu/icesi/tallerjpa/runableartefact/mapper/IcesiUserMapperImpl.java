package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-09T14:45:07-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class IcesiUserMapperImpl implements IcesiUserMapper {

    @Override
    public IcesiUser toIcesiUser(IcesiUserDTO icesiUserDTO) {
        if ( icesiUserDTO == null ) {
            return null;
        }

        IcesiUser icesiUser = new IcesiUser();

        icesiUser.setFirstName( icesiUserDTO.getFirstName() );
        icesiUser.setLastName( icesiUserDTO.getLastName() );
        icesiUser.setEmail( icesiUserDTO.getEmail() );
        icesiUser.setPhoneNumber( icesiUserDTO.getPhoneNumber() );
        icesiUser.setPassword( icesiUserDTO.getPassword() );

        return icesiUser;
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
}
