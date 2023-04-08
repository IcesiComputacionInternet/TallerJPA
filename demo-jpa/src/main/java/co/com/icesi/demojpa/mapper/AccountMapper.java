package co.com.icesi.demojpa.mapper;
import co.com.icesi.demojpa.dto.AccountCreateDTO;

import co.com.icesi.demojpa.dto.TransactionOperationDTO;
import co.com.icesi.demojpa.dto.TransactionResultDTO;
import co.com.icesi.demojpa.model.IcesiAccount;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiAccount fromIcesiAccountDTO (AccountCreateDTO accountCreateDTO);

    AccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);

    TransactionResultDTO fromTransactionOperation(TransactionOperationDTO operationDTO, String result);

}
