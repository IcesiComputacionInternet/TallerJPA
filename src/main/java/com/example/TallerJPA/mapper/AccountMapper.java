package com.example.TallerJPA.mapper;

import com.example.TallerJPA.dto.AccountCreateDTO;
import com.example.TallerJPA.dto.AccountResponseDTO;
import com.example.TallerJPA.dto.TransferResponseDTO;
import com.example.TallerJPA.model.IcesiAccount;
import com.example.TallerJPA.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {


    IcesiAccount fromAccountCreateDTO(AccountCreateDTO accountCreateDTO);

    AccountCreateDTO fromAccount(IcesiAccount icesiAccount);
    @Mapping(source = "icesiAccount.user.email", target = "userEmail")
    AccountResponseDTO fromAccountToResponse(IcesiAccount icesiAccount);
    @Mapping(source = "originAccount.accountNumber", target = "originAccountNumber")
    @Mapping(source = "destinationAccount.accountNumber", target = "destinationAccountNumber")
    @Mapping(source = "balance", target = "balance")
    TransferResponseDTO fromRequestToResponse(IcesiAccount originAccount, IcesiAccount destinationAccount, long amount, long balance);

}