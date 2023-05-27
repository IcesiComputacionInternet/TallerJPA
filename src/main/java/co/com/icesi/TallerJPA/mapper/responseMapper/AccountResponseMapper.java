package co.com.icesi.TallerJPA.mapper.responseMapper;

import co.com.icesi.TallerJPA.dto.TransactionOperationDTO;
import co.com.icesi.TallerJPA.dto.response.AccountResponseDTO;
import co.com.icesi.TallerJPA.dto.response.AccountsDTO;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountResponseMapper {

    AccountResponseDTO fromIcesiAccount(IcesiAccount icesiAccount);

    TransactionOperationDTO fromTransactionOperationDTO(TransactionOperationDTO transactionOperationDTO);

    AccountsDTO fromIcesiAccountToAccountsDTO(IcesiAccount icesiAccount);
}
