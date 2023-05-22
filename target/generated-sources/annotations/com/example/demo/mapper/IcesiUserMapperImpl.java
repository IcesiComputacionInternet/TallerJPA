package com.example.demo.mapper;

import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;
import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-21T18:17:18-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230413-0857, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class IcesiUserMapperImpl implements IcesiUserMapper {

    @Override
    public IcesiUser fromIcesiUserCreateDTO(IcesiUserCreateDTO icesiUserCreateDTO) {
        if ( icesiUserCreateDTO == null ) {
            return null;
        }

        IcesiUser.IcesiUserBuilder icesiUser = IcesiUser.builder();

        icesiUser.email( icesiUserCreateDTO.getEmail() );
        icesiUser.firstName( icesiUserCreateDTO.getFirstName() );
        icesiUser.lastName( icesiUserCreateDTO.getLastName() );
        icesiUser.password( icesiUserCreateDTO.getPassword() );
        icesiUser.phoneNumber( icesiUserCreateDTO.getPhoneNumber() );

        return icesiUser.build();
    }

    @Override
    public IcesiUserCreateDTO fromIcesiUserToIUserCreateDTO(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        IcesiUserCreateDTO.IcesiUserCreateDTOBuilder icesiUserCreateDTO = IcesiUserCreateDTO.builder();

        icesiUserCreateDTO.email( icesiUser.getEmail() );
        icesiUserCreateDTO.firstName( icesiUser.getFirstName() );
        icesiUserCreateDTO.lastName( icesiUser.getLastName() );
        icesiUserCreateDTO.password( icesiUser.getPassword() );
        icesiUserCreateDTO.phoneNumber( icesiUser.getPhoneNumber() );

        return icesiUserCreateDTO.build();
    }

    @Override
    public ResponseIcesiUserDTO fromIcesiUserToResponseIcesiUserDTO(IcesiUser icesiUser) {
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

    @Override
    public IcesiRole fromResponseIcesiUserDTO(ResponseIcesiUserDTO responseIcesiUserDTO) {
        if ( responseIcesiUserDTO == null ) {
            return null;
        }

        IcesiRole.IcesiRoleBuilder icesiRole = IcesiRole.builder();

        return icesiRole.build();
    }
}
