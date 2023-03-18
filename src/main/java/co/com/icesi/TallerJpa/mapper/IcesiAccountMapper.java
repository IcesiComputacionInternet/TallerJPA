package co.com.icesi.TallerJpa.mapper;

import co.com.icesi.TallerJpa.dto.IcesiAccountCreateDTO;
import co.com.icesi.TallerJpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromAccountDto(IcesiAccountCreateDTO icesiAccountCreateDTO);
    IcesiAccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
