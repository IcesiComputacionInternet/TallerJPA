package com.example.demo.mapper;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.DTO.ResponseTransactionDTO;
import com.example.demo.DTO.TransactionCreateDTO;
import com.example.demo.model.IcesiAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-22T08:59:22-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230413-0857, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class IcesiAccountMapperImpl implements IcesiAccountMapper {

    @Override
    public IcesiAccount fromIcesiAccountCreateDTO(IcesiAccountCreateDTO IcesiAccountCreateDTO) {
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
    public ResponseIcesiAccountDTO fromIcesiAccountToResponseIcesiAccountDTO(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        ResponseIcesiAccountDTO.ResponseIcesiAccountDTOBuilder responseIcesiAccountDTO = ResponseIcesiAccountDTO.builder();

        responseIcesiAccountDTO.accountNumber( icesiAccount.getAccountNumber() );
        responseIcesiAccountDTO.active( icesiAccount.isActive() );
        responseIcesiAccountDTO.balance( icesiAccount.getBalance() );
        responseIcesiAccountDTO.type( icesiAccount.getType() );

        return responseIcesiAccountDTO.build();
    }

    @Override
    public ResponseIcesiAccountDTO fromIcesiAccountCreateDTOToResponseIcesiAccountDTO(IcesiAccountCreateDTO icesiAccountCreateDTO) {
        if ( icesiAccountCreateDTO == null ) {
            return null;
        }

        ResponseIcesiAccountDTO.ResponseIcesiAccountDTOBuilder responseIcesiAccountDTO = ResponseIcesiAccountDTO.builder();

        responseIcesiAccountDTO.accountNumber( icesiAccountCreateDTO.getAccountNumber() );
        responseIcesiAccountDTO.active( icesiAccountCreateDTO.isActive() );
        responseIcesiAccountDTO.balance( icesiAccountCreateDTO.getBalance() );
        responseIcesiAccountDTO.type( icesiAccountCreateDTO.getType() );

        return responseIcesiAccountDTO.build();
    }

    @Override
    public ResponseTransactionDTO fromTransactionCrateDTO(TransactionCreateDTO transactionCreateDTO) {
        if ( transactionCreateDTO == null ) {
            return null;
        }

        ResponseTransactionDTO.ResponseTransactionDTOBuilder responseTransactionDTO = ResponseTransactionDTO.builder();

        responseTransactionDTO.receiverAccountNumber( transactionCreateDTO.getReceiverAccountNumber() );
        responseTransactionDTO.senderAccountNumber( transactionCreateDTO.getSenderAccountNumber() );

        return responseTransactionDTO.build();
    }
}
