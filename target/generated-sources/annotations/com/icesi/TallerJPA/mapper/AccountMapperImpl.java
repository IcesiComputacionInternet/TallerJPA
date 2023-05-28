package com.icesi.TallerJPA.mapper;

import com.icesi.TallerJPA.dto.request.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.request.IcesiRoleDTO;
import com.icesi.TallerJPA.dto.response.IcesiAccountResponseDTO;
import com.icesi.TallerJPA.dto.response.IcesiUserResponseDTO;
import com.icesi.TallerJPA.model.IcesiAccount;
import com.icesi.TallerJPA.model.IcesiRole;
import com.icesi.TallerJPA.model.IcesiUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-23T15:11:00-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public IcesiAccount fromIcesiAccountDTO(IcesiAccountDTO icesiAccountDTO) {
        if ( icesiAccountDTO == null ) {
            return null;
        }

        IcesiAccount.IcesiAccountBuilder icesiAccount = IcesiAccount.builder();

        icesiAccount.balance( icesiAccountDTO.getBalance() );
        icesiAccount.type( icesiAccountDTO.getType() );
        icesiAccount.active( icesiAccountDTO.getActive() );

        return icesiAccount.build();
    }

    @Override
    public IcesiAccountDTO fromIcesiAccount(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        IcesiAccountDTO.IcesiAccountDTOBuilder icesiAccountDTO = IcesiAccountDTO.builder();

        icesiAccountDTO.balance( icesiAccount.getBalance() );
        icesiAccountDTO.type( icesiAccount.getType() );
        icesiAccountDTO.active( icesiAccount.getActive() );

        return icesiAccountDTO.build();
    }

    @Override
    public IcesiAccountResponseDTO toResponse(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        IcesiAccountResponseDTO.IcesiAccountResponseDTOBuilder icesiAccountResponseDTO = IcesiAccountResponseDTO.builder();

        icesiAccountResponseDTO.accountId( icesiAccount.getAccountId() );
        icesiAccountResponseDTO.accountNumber( icesiAccount.getAccountNumber() );
        icesiAccountResponseDTO.balance( icesiAccount.getBalance() );
        if ( icesiAccount.getType() != null ) {
            icesiAccountResponseDTO.type( icesiAccount.getType().name() );
        }
        icesiAccountResponseDTO.active( icesiAccount.getActive() );
        icesiAccountResponseDTO.icesiUser( icesiUserToIcesiUserResponseDTO( icesiAccount.getIcesiUser() ) );

        return icesiAccountResponseDTO.build();
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

    protected IcesiUserResponseDTO icesiUserToIcesiUserResponseDTO(IcesiUser icesiUser) {
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
}
