package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "user", source = "user",ignore=true)
    IcesiAccount fromIcesiAccountDTO(RequestAccountDTO accountReqDTO);
    @Mapping(target = "user", source = "user",ignore=true)
    ResponseAccountDTO fromIcesiAccountToResUserDTO(IcesiAccount icesiAccount);
    //RequestAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
