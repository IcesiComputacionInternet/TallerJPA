package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.TransactionInformationDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.TransactionInformationResponseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-22T08:18:24-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionInformationResponseDTO toTransactionInformationResponseDTO(TransactionInformationDTO transactionInformationDTO) {
        if ( transactionInformationDTO == null ) {
            return null;
        }

        TransactionInformationResponseDTO.TransactionInformationResponseDTOBuilder transactionInformationResponseDTO = TransactionInformationResponseDTO.builder();

        transactionInformationResponseDTO.accountNumberOrigin( transactionInformationDTO.getAccountNumberOrigin() );
        transactionInformationResponseDTO.accountNumberDestination( transactionInformationDTO.getAccountNumberDestination() );
        if ( transactionInformationDTO.getAmount() != null ) {
            transactionInformationResponseDTO.amount( transactionInformationDTO.getAmount() );
        }

        return transactionInformationResponseDTO.build();
    }

    @Override
    public TransactionInformationDTO toTransactionInformationDTO(TransactionInformationResponseDTO transactionInformationResponseDTO) {
        if ( transactionInformationResponseDTO == null ) {
            return null;
        }

        TransactionInformationDTO.TransactionInformationDTOBuilder transactionInformationDTO = TransactionInformationDTO.builder();

        transactionInformationDTO.accountNumberOrigin( transactionInformationResponseDTO.getAccountNumberOrigin() );
        transactionInformationDTO.accountNumberDestination( transactionInformationResponseDTO.getAccountNumberDestination() );
        transactionInformationDTO.amount( transactionInformationResponseDTO.getAmount() );

        return transactionInformationDTO.build();
    }
}
