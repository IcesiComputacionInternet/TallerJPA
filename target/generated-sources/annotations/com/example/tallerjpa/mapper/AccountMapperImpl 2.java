package com.example.tallerjpa.mapper;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.dto.AccountResponseDTO;
import com.example.tallerjpa.enums.AccountType;
import com.example.tallerjpa.model.IcesiAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-28T12:35:59-0500",
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
        if ( accountDTO.getType() != null ) {
            icesiAccount.type( Enum.valueOf( AccountType.class, accountDTO.getType() ) );
        }
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
        if ( icesiAccount.getType() != null ) {
            accountDTO.type( icesiAccount.getType().name() );
        }
        accountDTO.active( icesiAccount.isActive() );

        return accountDTO.build();
    }

    @Override
    public AccountResponseDTO fromAccountToResponseDTO(IcesiAccount account) {
        if ( account == null ) {
            return null;
        }

        AccountResponseDTO.AccountResponseDTOBuilder accountResponseDTO = AccountResponseDTO.builder();

        accountResponseDTO.accountNumber( account.getAccountNumber() );
        accountResponseDTO.balance( account.getBalance() );
        accountResponseDTO.type( account.getType() );
        accountResponseDTO.active( account.isActive() );

        return accountResponseDTO.build();
    }
}
