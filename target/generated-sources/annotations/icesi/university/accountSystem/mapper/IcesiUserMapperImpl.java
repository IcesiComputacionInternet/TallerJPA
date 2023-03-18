package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.IcesiUserDTO;
import icesi.university.accountSystem.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-18T09:39:36-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
@Component
public class IcesiUserMapperImpl implements IcesiUserMapper {

    @Override
    public IcesiUser fromIcesiUserDTO(IcesiUserDTO icesiUserDTO) {
        if ( icesiUserDTO == null ) {
            return null;
        }

        IcesiUser.IcesiUserBuilder icesiUser = IcesiUser.builder();

        icesiUser.userId( icesiUserDTO.getUserId() );
        icesiUser.firstName( icesiUserDTO.getFirstName() );
        icesiUser.lastName( icesiUserDTO.getLastName() );
        icesiUser.email( icesiUserDTO.getEmail() );
        icesiUser.phoneNumber( icesiUserDTO.getPhoneNumber() );
        icesiUser.password( icesiUserDTO.getPassword() );
        icesiUser.role( icesiUserDTO.getRole() );

        return icesiUser.build();
    }

    @Override
    public IcesiUserDTO fromIcesiUser(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        IcesiUserDTO.IcesiUserDTOBuilder icesiUserDTO = IcesiUserDTO.builder();

        icesiUserDTO.userId( icesiUser.getUserId() );
        icesiUserDTO.firstName( icesiUser.getFirstName() );
        icesiUserDTO.lastName( icesiUser.getLastName() );
        icesiUserDTO.email( icesiUser.getEmail() );
        icesiUserDTO.phoneNumber( icesiUser.getPhoneNumber() );
        icesiUserDTO.password( icesiUser.getPassword() );
        icesiUserDTO.role( icesiUser.getRole() );

        return icesiUserDTO.build();
    }
}
