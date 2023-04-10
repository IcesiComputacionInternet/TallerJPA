package com.example.tallerjpa.mapper;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiAccount fromAccountDTO (AccountDTO accountDTO);

    AccountDTO fromIcesiAccount (IcesiAccount icesiAccount);

}
