package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-22T04:14:30-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public IcesiUser fromIcesiUserDTO(RequestUserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        IcesiUser.IcesiUserBuilder icesiUser = IcesiUser.builder();

        icesiUser.firstName( userDTO.getFirstName() );
        icesiUser.lastName( userDTO.getLastName() );
        icesiUser.email( userDTO.getEmail() );
        icesiUser.phoneNumber( userDTO.getPhoneNumber() );
        icesiUser.password( userDTO.getPassword() );

        return icesiUser.build();
    }

    @Override
    public ResponseUserDTO fromUserToRespUserDTO(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        ResponseUserDTO.ResponseUserDTOBuilder responseUserDTO = ResponseUserDTO.builder();

        responseUserDTO.firstName( icesiUser.getFirstName() );
        responseUserDTO.lastName( icesiUser.getLastName() );
        responseUserDTO.email( icesiUser.getEmail() );
        responseUserDTO.password( icesiUser.getPassword() );
        responseUserDTO.phoneNumber( icesiUser.getPhoneNumber() );

        return responseUserDTO.build();
    }
}
