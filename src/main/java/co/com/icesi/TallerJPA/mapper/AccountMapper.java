package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.AccountCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "user", source = "user",ignore = true)
    IcesiAccount fromIcesiAccountDTO(AccountCreateDTO accountCreateDTO);

    @Mapping(target = "user", source = "user",ignore = true)
    AccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
