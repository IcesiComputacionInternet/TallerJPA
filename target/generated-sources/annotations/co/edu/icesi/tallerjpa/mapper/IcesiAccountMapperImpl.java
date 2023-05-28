package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-28T13:14:02-0500",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230413-0857, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class IcesiAccountMapperImpl implements IcesiAccountMapper {

    @Override
    public IcesiAccount toIcesiAccount(IcesiAccountDTO icesiAccountDTO) {
        if ( icesiAccountDTO == null ) {
            return null;
        }

        IcesiAccount.IcesiAccountBuilder icesiAccount = IcesiAccount.builder();

        icesiAccount.active( icesiAccountDTO.isActive() );
        if ( icesiAccountDTO.getBalance() != null ) {
            icesiAccount.balance( icesiAccountDTO.getBalance() );
        }
        icesiAccount.type( icesiAccountDTO.getType() );

        return icesiAccount.build();
    }

    @Override
    public IcesiAccountDTO toIcesiAccountDTO(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        IcesiAccountDTO.IcesiAccountDTOBuilder icesiAccountDTO = IcesiAccountDTO.builder();

        icesiAccountDTO.active( icesiAccount.getActive() );
        icesiAccountDTO.balance( icesiAccount.getBalance() );
        icesiAccountDTO.type( icesiAccount.getType() );

        return icesiAccountDTO.build();
    }
}
