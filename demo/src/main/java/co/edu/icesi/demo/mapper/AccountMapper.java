package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiAccount fromIcesiAccountDTO(AccountCreateDTO accountCreateDTO);

}
