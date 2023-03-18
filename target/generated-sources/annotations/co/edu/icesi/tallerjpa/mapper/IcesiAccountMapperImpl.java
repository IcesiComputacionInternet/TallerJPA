package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-18T11:48:09-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class IcesiAccountMapperImpl implements IcesiAccountMapper {

    @Override
    public IcesiAccount toIcesiAccount(IcesiAccountDTO icesiAccountDTO) {
        if ( icesiAccountDTO == null ) {
            return null;
        }

        IcesiAccount.IcesiAccountBuilder icesiAccount = IcesiAccount.builder();

        if ( icesiAccountDTO.getBalance() != null ) {
            icesiAccount.balance( icesiAccountDTO.getBalance() );
        }
        icesiAccount.type( icesiAccountDTO.getType() );
        icesiAccount.active( icesiAccountDTO.isActive() );

        return icesiAccount.build();
    }

    @Override
    public IcesiAccountDTO toIcesiAccountDTO(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        IcesiAccountDTO.IcesiAccountDTOBuilder icesiAccountDTO = IcesiAccountDTO.builder();

        icesiAccountDTO.balance( icesiAccount.getBalance() );
        icesiAccountDTO.type( icesiAccount.getType() );
        icesiAccountDTO.active( icesiAccount.isActive() );

        return icesiAccountDTO.build();
    }
}
