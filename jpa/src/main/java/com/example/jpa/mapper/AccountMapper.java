package com.example.jpa.mapper;

import com.example.jpa.dto.AccountRequestDTO;
import com.example.jpa.dto.AccountResponseDTO;
import com.example.jpa.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "icesiUser", source = "icesiUser", ignore = true)
    IcesiAccount fromAccountDTO(AccountRequestDTO createdAccountDTO);

    @Mapping(target = "icesiUser", source = "icesiUser", ignore = true)
    AccountRequestDTO fromAccount(IcesiAccount account);

    AccountResponseDTO fromAccountToSendAccountDTO(IcesiAccount account);
}
