package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.Enum.AccountType;
import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.model.IcesiAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-25T15:39:12-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public IcesiAccount fromIcesiAccountDTO(RequestAccountDTO accountReqDTO) {
        if ( accountReqDTO == null ) {
            return null;
        }

        IcesiAccount.IcesiAccountBuilder icesiAccount = IcesiAccount.builder();

        if ( accountReqDTO.getBalance() != null ) {
            icesiAccount.balance( accountReqDTO.getBalance() );
        }
        if ( accountReqDTO.getType() != null ) {
            icesiAccount.type( accountReqDTO.getType().name() );
        }

        return icesiAccount.build();
    }

    @Override
    public ResponseAccountDTO fromIcesiAccountToResUserDTO(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        ResponseAccountDTO.ResponseAccountDTOBuilder responseAccountDTO = ResponseAccountDTO.builder();

        responseAccountDTO.accountNumber( icesiAccount.getAccountNumber() );
        responseAccountDTO.balance( icesiAccount.getBalance() );
        if ( icesiAccount.getType() != null ) {
            responseAccountDTO.type( Enum.valueOf( AccountType.class, icesiAccount.getType() ) );
        }
        responseAccountDTO.active( icesiAccount.isActive() );

        return responseAccountDTO.build();
    }
}
