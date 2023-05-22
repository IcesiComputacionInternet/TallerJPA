package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.AccountDTO;
import co.edu.icesi.demo.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiAccount fromIcesiAccountDTO(AccountDTO accountDTO);

    @Mapping(target = "userEmail", expression = "java(icesiAccount.getUser().getEmail())")
    AccountDTO fromIcesiAccount(IcesiAccount icesiAccount);


}
