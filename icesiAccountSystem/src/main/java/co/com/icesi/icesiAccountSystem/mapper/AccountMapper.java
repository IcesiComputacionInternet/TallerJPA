package co.com.icesi.icesiAccountSystem.mapper;

import co.com.icesi.icesiAccountSystem.dto.RequestAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseAccountDTO;
import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "user", source = "userEmail",ignore=true)
    @Mapping(target = "type", source = "type",ignore=true)
    IcesiAccount fromAccountDTO(RequestAccountDTO requestAccountDTO);
    @Mapping(target = "user", source = "user",ignore=true)
    ResponseAccountDTO fromAccountToResponseAccountDTO(IcesiAccount icesiAccount);
}
