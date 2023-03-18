package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.IcesiAccountDTO;
import icesi.university.accountSystem.model.IcesiAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-18T09:39:36-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
@Component
public class IcesiAccountMapperImpl implements IcesiAccountMapper {

    @Override
    public IcesiAccount fromIcesiAccountDTO(IcesiAccountDTO icesiAccountDTO) {
        if ( icesiAccountDTO == null ) {
            return null;
        }

        IcesiAccount.IcesiAccountBuilder icesiAccount = IcesiAccount.builder();

        icesiAccount.accountId( icesiAccountDTO.getAccountId() );
        icesiAccount.accountNumber( icesiAccountDTO.getAccountNumber() );
        icesiAccount.balance( icesiAccountDTO.getBalance() );
        icesiAccount.type( icesiAccountDTO.getType() );
        icesiAccount.active( icesiAccountDTO.isActive() );
        icesiAccount.user( icesiAccountDTO.getUser() );

        return icesiAccount.build();
    }

    @Override
    public IcesiAccountDTO fromIcesiAccount(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        IcesiAccountDTO.IcesiAccountDTOBuilder icesiAccountDTO = IcesiAccountDTO.builder();

        icesiAccountDTO.accountId( icesiAccount.getAccountId() );
        icesiAccountDTO.accountNumber( icesiAccount.getAccountNumber() );
        icesiAccountDTO.balance( icesiAccount.getBalance() );
        icesiAccountDTO.type( icesiAccount.getType() );
        icesiAccountDTO.active( icesiAccount.isActive() );
        icesiAccountDTO.user( icesiAccount.getUser() );

        return icesiAccountDTO.build();
    }
}
