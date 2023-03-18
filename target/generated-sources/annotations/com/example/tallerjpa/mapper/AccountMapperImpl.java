package com.example.tallerjpa.mapper;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.model.IcesiAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-18T06:38:17-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public IcesiAccount fromAccountDTO(AccountDTO accountDTO) {
        if ( accountDTO == null ) {
            return null;
        }

        IcesiAccount.IcesiAccountBuilder icesiAccount = IcesiAccount.builder();

        icesiAccount.balance( accountDTO.getBalance() );
        icesiAccount.type( accountDTO.getType() );
        icesiAccount.active( accountDTO.isActive() );

        return icesiAccount.build();
    }

    @Override
    public AccountDTO fromIcesiAccount(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        AccountDTO.AccountDTOBuilder accountDTO = AccountDTO.builder();

        accountDTO.balance( icesiAccount.getBalance() );
        accountDTO.type( icesiAccount.getType() );
        accountDTO.active( icesiAccount.isActive() );

        return accountDTO.build();
    }
}
