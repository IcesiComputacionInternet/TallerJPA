package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.IcesiAccountResponseDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-04-20T15:37:34-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class IcesiAccountMapperImpl implements IcesiAccountMapper {

    @Override
    public IcesiAccount toIcesiAccount(IcesiAccountDTO icesiAccountDTO) {
        if ( icesiAccountDTO == null ) {
            return null;
        }

        IcesiAccount.IcesiAccountBuilder icesiAccount = IcesiAccount.builder();

        icesiAccount.balance( icesiAccountDTO.getBalance() );
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

    @Override
    public IcesiAccountDTO toIcesiAccountDTO(IcesiAccountResponseDTO icesiAccountResponseDTO) {
        if ( icesiAccountResponseDTO == null ) {
            return null;
        }

        IcesiAccountDTO.IcesiAccountDTOBuilder icesiAccountDTO = IcesiAccountDTO.builder();

        return icesiAccountDTO.build();
    }

    @Override
    public IcesiAccountResponseDTO toIcesiAccountResponseDTO(IcesiAccount icesiAccount) {
        if ( icesiAccount == null ) {
            return null;
        }

        IcesiAccountResponseDTO icesiAccountResponseDTO = new IcesiAccountResponseDTO();

        return icesiAccountResponseDTO;
    }

    @Override
    public IcesiAccountResponseDTO toIcesiAccountResponseDTO(IcesiAccountDTO icesiAccountDTO) {
        if ( icesiAccountDTO == null ) {
            return null;
        }

        IcesiAccountResponseDTO icesiAccountResponseDTO = new IcesiAccountResponseDTO();

        return icesiAccountResponseDTO;
    }
}
