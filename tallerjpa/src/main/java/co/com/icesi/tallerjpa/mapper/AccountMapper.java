package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.AccountCreateDTO;
import co.com.icesi.tallerjpa.model.IcesiAccount;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface AccountMapper {
    IcesiAccount fromIcesiAccountDTO(AccountCreateDTO accountCreateDTO);
    AccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
