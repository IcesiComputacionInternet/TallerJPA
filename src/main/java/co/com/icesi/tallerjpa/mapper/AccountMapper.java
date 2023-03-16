package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "user", source = "user",ignore=true)
    Account fromAccountDTO(RequestAccountDTO createdAccountDTO);
    @Mapping(target = "user", source = "user",ignore=true)
    RequestAccountDTO fromAccount(Account account);
    ResponseAccountDTO fromAccountToSendAccountDTO(Account account);
}
