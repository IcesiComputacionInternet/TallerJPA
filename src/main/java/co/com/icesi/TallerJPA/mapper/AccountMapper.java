package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.AccountCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiAccount fromIcesiAccountDTO(AccountCreateDTO accountCreateDTO);
    AccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
