package com.example.tallerjpa.mapper;

import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-18T06:38:17-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public IcesiUser fromUserDTO(UserDTO userDTO) {
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
    public UserDTO fromIcesiUser(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.firstName( icesiUser.getFirstName() );
        userDTO.lastName( icesiUser.getLastName() );
        userDTO.email( icesiUser.getEmail() );
        userDTO.phoneNumber( icesiUser.getPhoneNumber() );
        userDTO.password( icesiUser.getPassword() );

        return userDTO.build();
    }
}
