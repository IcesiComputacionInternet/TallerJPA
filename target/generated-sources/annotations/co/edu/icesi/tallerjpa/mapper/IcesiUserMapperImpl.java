package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-18T11:48:09-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
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
    public IcesiUserDTO fromModel(IcesiUser icesiUser) {
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
