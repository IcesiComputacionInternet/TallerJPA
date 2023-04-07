package com.example.demo.mapper;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.model.IcesiAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-07T11:27:10-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20230218-1114, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class IcesiAccountMapperImpl implements IcesiAccountMapper {

    @Override
    public IcesiAccount fromIcesiAccountDTO(IcesiAccountCreateDTO IcesiAccountCreateDTO) {
        if ( IcesiAccountCreateDTO == null ) {
            return null;
        }

        IcesiAccount.IcesiAccountBuilder icesiAccount = IcesiAccount.builder();

        icesiAccount.accountNumber( IcesiAccountCreateDTO.getAccountNumber() );
        icesiAccount.active( IcesiAccountCreateDTO.isActive() );
        icesiAccount.balance( IcesiAccountCreateDTO.getBalance() );
        icesiAccount.icesiUser( IcesiAccountCreateDTO.getIcesiUser() );
        icesiAccount.type( IcesiAccountCreateDTO.getType() );

        return icesiAccount.build();
    }

    @Override
    public IcesiAccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        IcesiAccountCreateDTO.IcesiAccountCreateDTOBuilder icesiAccountCreateDTO = IcesiAccountCreateDTO.builder();

        icesiAccountCreateDTO.accountNumber( icesiAccount.getAccountNumber() );
        icesiAccountCreateDTO.active( icesiAccount.isActive() );
        icesiAccountCreateDTO.balance( icesiAccount.getBalance() );
        icesiAccountCreateDTO.icesiUser( icesiAccount.getIcesiUser() );
        icesiAccountCreateDTO.type( icesiAccount.getType() );

        return icesiAccountCreateDTO.build();
    }

    @Override
    public ResponseIcesiAccountDTO fromIcesiAcountToIcesiAccountDTO(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        ResponseIcesiAccountDTO responseIcesiAccountDTO = new ResponseIcesiAccountDTO();

        return responseIcesiAccountDTO;
    }
}
