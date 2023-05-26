package co.com.icesi.jpataller.mapper.response;

import co.com.icesi.jpataller.dto.response.IcesiResponseAccountDTO;
import co.com.icesi.jpataller.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountResponseMapper {

    IcesiResponseAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);
}