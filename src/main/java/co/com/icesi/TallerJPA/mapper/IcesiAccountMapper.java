package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiAccountCreateDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiAccountCreateResponseDTO;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromIcesiAccountDTO(IcesiAccountCreateDTO icesiAccountDTO);
    IcesiAccountCreateResponseDTO fromIcesiAccount(IcesiAccount icesiAccount);
    IcesiAccountCreateResponseDTO accountToAccountDTO(IcesiAccount icesiAccount);
}