package com.edu.icesi.TallerJPA.mapper;

import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.dto.TransactionDTO;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "icesiUser", source = "icesiUserId",ignore=true)
    IcesiAccount fromIcesiAccountDTO(AccountCreateDTO accountCreateDTO);

    @Mapping(target = "icesiUserId", expression = "java(String.valueOf(icesiAccount.getIcesiUser().getUserId()))")
    AccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
