package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.IcesiAccountCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromIcesiAccountCreateDTO(IcesiAccountCreateDTO icesiAccountCreateDTO);
    IcesiAccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
