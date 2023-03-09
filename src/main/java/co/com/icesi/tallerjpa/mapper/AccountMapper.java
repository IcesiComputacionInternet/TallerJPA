package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.AccountDTO;
import co.com.icesi.tallerjpa.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account fromAccountDTO(AccountDTO accountDTO);
    AccountDTO fromAccount(Account account);
}
