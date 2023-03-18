package co.com.icesi.demojpa.mapper;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiAccount fromIcesiAccountDTO(AccountCreateDTO accountCreateDTO);

    AccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
