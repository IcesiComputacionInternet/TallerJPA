package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import co.edu.icesi.demo.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiAccount fromIcesiAccountDTO(AccountCreateDTO accountCreateDTO);

    @Mapping(target = "userEmail", expression = "java(icesiAccount.getUser().getEmail())")
    AccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);


}
