package com.example.TallerJPA.mapper;

import com.example.TallerJPA.dto.AccountCreateDTO;
import com.example.TallerJPA.model.IcesiAccount;
import com.example.TallerJPA.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {


    IcesiAccount fromAccountCreateDTO(AccountCreateDTO accountCreateDTO);

    AccountCreateDTO fromAccount(IcesiAccount icesiAccount);
}