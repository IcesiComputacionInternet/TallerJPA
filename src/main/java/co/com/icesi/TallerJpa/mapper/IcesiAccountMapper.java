package co.com.icesi.TallerJpa.mapper;

import co.com.icesi.TallerJpa.dto.IcesiAccountRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiAccountResponseDTO;
import co.com.icesi.TallerJpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromAccountDto(IcesiAccountRequestDTO icesiAccountRequestDTO);
    IcesiAccountResponseDTO fromIcesiAccountToResponse(IcesiAccount icesiAccount);
}
