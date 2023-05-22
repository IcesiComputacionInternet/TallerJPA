package co.edu.icesi.tallerJPA.mapper;

import co.edu.icesi.tallerJPA.dto.AccountDTO;
import co.edu.icesi.tallerJPA.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account fromAccountDTO(AccountDTO accountDTO);
    AccountDTO fromAccount(Account account);
}
