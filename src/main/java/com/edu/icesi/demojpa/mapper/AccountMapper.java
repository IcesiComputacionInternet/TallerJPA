package com.edu.icesi.demojpa.mapper;

import com.edu.icesi.demojpa.dto.AccountCreateDTO;
import com.edu.icesi.demojpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    IcesiAccount fromIcesiAccountDTO(AccountCreateDTO accountCreateDTO);
}
