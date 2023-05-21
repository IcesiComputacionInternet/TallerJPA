package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.*;
import icesi.university.accountSystem.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    @Mapping(target = "user", source = "user",ignore=true)
    IcesiAccount fromIcesiAccountDTO(RequestAccountDTO createdAccountDTO);
    @Mapping(target = "user", source = "user",ignore=true)
    RequestAccountDTO fromIcesiAccount(IcesiAccount account);
    ResponseAccountDTO fromAccountToSendAccountDTO(IcesiAccount account);

    TransactionResultDTO fromTransactionOperation(TransactionOperationDTO operationDTO, String result);
}
