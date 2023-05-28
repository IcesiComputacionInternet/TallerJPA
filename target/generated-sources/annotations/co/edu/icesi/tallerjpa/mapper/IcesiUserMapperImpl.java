package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-28T13:14:02-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230413-0857, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class IcesiUserMapperImpl implements IcesiUserMapper {

    @Override
    public IcesiUser toIcesiUser(IcesiUserDTO icesiUserDTO) {
        if ( icesiUserDTO == null ) {
            return null;
        }

        IcesiUser.IcesiUserBuilder icesiUser = IcesiUser.builder();

        icesiUser.email( icesiUserDTO.getEmail() );
        icesiUser.firstName( icesiUserDTO.getFirstName() );
        icesiUser.lastName( icesiUserDTO.getLastName() );
        icesiUser.password( icesiUserDTO.getPassword() );
        icesiUser.phoneNumber( icesiUserDTO.getPhoneNumber() );

        return icesiUser.build();
    }

    @Override
    public IcesiUserDTO fromModel(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        IcesiUserDTO.IcesiUserDTOBuilder icesiUserDTO = IcesiUserDTO.builder();

        icesiUserDTO.email( icesiUser.getEmail() );
        icesiUserDTO.firstName( icesiUser.getFirstName() );
        icesiUserDTO.lastName( icesiUser.getLastName() );
        icesiUserDTO.password( icesiUser.getPassword() );
        icesiUserDTO.phoneNumber( icesiUser.getPhoneNumber() );

        return icesiUserDTO.build();
    }
}
