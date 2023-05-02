package com.example.demo.mapper;

import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;
import com.example.demo.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-02T14:37:38-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20230213-1046, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class IcesiUserMapperImpl implements IcesiUserMapper {

    @Override
    public IcesiUser fromIcesiUserDTO(IcesiUserCreateDTO userCreateDTO) {
        if ( userCreateDTO == null ) {
            return null;
        }

        IcesiUser.IcesiUserBuilder icesiUser = IcesiUser.builder();

        icesiUser.email( userCreateDTO.getEmail() );
        icesiUser.firstName( userCreateDTO.getFirstName() );
        icesiUser.icesiRole( userCreateDTO.getIcesiRole() );
        icesiUser.lastName( userCreateDTO.getLastName() );
        icesiUser.password( userCreateDTO.getPassword() );
        icesiUser.phoneNumber( userCreateDTO.getPhoneNumber() );

        return icesiUser.build();
    }

    @Override
    public IcesiUserCreateDTO fromIcesiUser(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        IcesiUserCreateDTO.IcesiUserCreateDTOBuilder icesiUserCreateDTO = IcesiUserCreateDTO.builder();

        icesiUserCreateDTO.email( icesiUser.getEmail() );
        icesiUserCreateDTO.firstName( icesiUser.getFirstName() );
        icesiUserCreateDTO.icesiRole( icesiUser.getIcesiRole() );
        icesiUserCreateDTO.lastName( icesiUser.getLastName() );
        icesiUserCreateDTO.password( icesiUser.getPassword() );
        icesiUserCreateDTO.phoneNumber( icesiUser.getPhoneNumber() );

        return icesiUserCreateDTO.build();
    }

    @Override
    public ResponseIcesiUserDTO fromIcesiUserToIcesiUserCreateDTO(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        ResponseIcesiUserDTO.ResponseIcesiUserDTOBuilder responseIcesiUserDTO = ResponseIcesiUserDTO.builder();

        responseIcesiUserDTO.email( icesiUser.getEmail() );
        responseIcesiUserDTO.firstName( icesiUser.getFirstName() );
        responseIcesiUserDTO.lastName( icesiUser.getLastName() );
        responseIcesiUserDTO.password( icesiUser.getPassword() );
        responseIcesiUserDTO.phoneNumber( icesiUser.getPhoneNumber() );

        return responseIcesiUserDTO.build();
    }
}
