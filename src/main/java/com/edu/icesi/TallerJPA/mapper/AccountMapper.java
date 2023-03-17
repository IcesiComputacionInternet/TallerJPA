package com.edu.icesi.TallerJPA.mapper;

import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiAccount fromIcesiAccountDTO(AccountCreateDTO accountCreateDTO);
}
