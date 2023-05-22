package co.com.icesi.demojpa.mapper;

import co.com.icesi.demojpa.dto.request.AccountCreateDTO;
import co.com.icesi.demojpa.dto.response.AccountResponseDTO;
import co.com.icesi.demojpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    IcesiAccount fromAccountCreateDTO(AccountCreateDTO createdAccountDTO);
    AccountCreateDTO fromIcesiAccount(IcesiAccount account);

    AccountResponseDTO toResponse(IcesiAccount icesiAccount);
}
