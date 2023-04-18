package com.icesi.TallerJPA.mapper;

import com.icesi.TallerJPA.dto.request.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.response.IcesiAccountResponseDTO;
import com.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiAccount fromIcesiAccountDTO(IcesiAccountDTO icesiAccountDTO);

    IcesiAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);

    IcesiAccountResponseDTO toResponse(IcesiAccount icesiAccount);
}
