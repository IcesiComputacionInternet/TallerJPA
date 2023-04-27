package co.com.icesi.TallerJpa.mapper;

import co.com.icesi.TallerJpa.dto.IcesiAccountRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiAccountResponseDTO;
import co.com.icesi.TallerJpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromAccountDto(IcesiAccountRequestDTO icesiAccountRequestDTO);
    IcesiAccountRequestDTO fromIcesiAccount(IcesiAccount icesiAccount);
    IcesiAccountResponseDTO fromIcesiAccountResponse(IcesiAccount icesiAccount);
}
