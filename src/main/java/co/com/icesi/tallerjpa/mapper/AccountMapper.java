package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.CreatedAccountDTO;
import co.com.icesi.tallerjpa.dto.SendAccountDTO;
import co.com.icesi.tallerjpa.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "user", source = "user",ignore=true)
    Account fromAccountDTO(CreatedAccountDTO createdAccountDTO);
    @Mapping(target = "user", source = "user",ignore=true)
    CreatedAccountDTO fromAccount(Account account);
    SendAccountDTO fromAccountToSendAccountDTO(Account account);
}
