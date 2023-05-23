package com.icesi.TallerJPA.mapper;

import com.icesi.TallerJPA.dto.request.IcesiRoleDTO;
import com.icesi.TallerJPA.dto.request.IcesiUserDTO;
import com.icesi.TallerJPA.dto.response.IcesiUserResponseDTO;
import com.icesi.TallerJPA.model.IcesiRole;
import com.icesi.TallerJPA.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-23T14:57:20-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public IcesiUser fromIcesiUser(IcesiUserDTO icesiUserDTO) {
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
    public IcesiUserDTO fromIcesiUserDTO(IcesiUser icesiUser) {
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
    public IcesiUserResponseDTO toResponse(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        IcesiUserResponseDTO.IcesiUserResponseDTOBuilder icesiUserResponseDTO = IcesiUserResponseDTO.builder();

        icesiUserResponseDTO.userId( icesiUser.getUserId() );
        icesiUserResponseDTO.firstName( icesiUser.getFirstName() );
        icesiUserResponseDTO.lastName( icesiUser.getLastName() );
        icesiUserResponseDTO.email( icesiUser.getEmail() );
        icesiUserResponseDTO.phoneNumber( icesiUser.getPhoneNumber() );
        icesiUserResponseDTO.password( icesiUser.getPassword() );
        icesiUserResponseDTO.icesiRole( icesiRoleToIcesiRoleDTO( icesiUser.getIcesiRole() ) );

        return icesiUserResponseDTO.build();
    }

    protected IcesiRoleDTO icesiRoleToIcesiRoleDTO(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        IcesiRoleDTO.IcesiRoleDTOBuilder icesiRoleDTO = IcesiRoleDTO.builder();

        icesiRoleDTO.description( icesiRole.getDescription() );
        icesiRoleDTO.name( icesiRole.getName() );

        return icesiRoleDTO.build();
    }
}
