package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.TransactionInformationDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.TransactionInformationResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionInformationResponseDTO toTransactionInformationResponseDTO(TransactionInformationDTO transactionInformationDTO);

    TransactionInformationDTO toTransactionInformationDTO(TransactionInformationResponseDTO transactionInformationResponseDTO);
}

