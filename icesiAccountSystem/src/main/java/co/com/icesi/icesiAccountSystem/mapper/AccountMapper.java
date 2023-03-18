package co.com.icesi.icesiAccountSystem.mapper;

import co.com.icesi.icesiAccountSystem.dto.AccountDTO;
import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    IcesiAccount fromAccountDTO(AccountDTO accountDTO);
}
