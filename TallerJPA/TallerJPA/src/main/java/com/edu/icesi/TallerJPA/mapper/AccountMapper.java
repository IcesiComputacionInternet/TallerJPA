package com.edu.icesi.TallerJPA.mapper;

import com.edu.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.edu.icesi.TallerJPA.dto.IcesiAccountGetDTO;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    IcesiAccountGetDTO fromIcesiAccountGet(IcesiAccount accountCreateDTO);

    IcesiAccount fromIcesiAccountDTO(IcesiAccountDTO accountCreateDTO);

    IcesiAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
