package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiAccountCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiAccountShowDTO;
import co.edu.icesi.tallerjpa.dto.TransactionCreateDTO;
import co.edu.icesi.tallerjpa.dto.TransactionResultDTO;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromCreateIcesiAccountDTO(IcesiAccountCreateDTO icesiAccountCreateDTO);
    IcesiAccountShowDTO fromIcesiAccountToShowDTO(IcesiAccount icesiAccount);

    TransactionResultDTO fromTransactionCreateDTO(TransactionCreateDTO transactionCreateDTO);
}
