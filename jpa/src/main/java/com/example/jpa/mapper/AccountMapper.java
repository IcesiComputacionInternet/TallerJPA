package com.example.jpa.mapper;

import com.example.jpa.dto.AccountRequestDTO;
import com.example.jpa.dto.AccountResponseDTO;
import com.example.jpa.dto.TransactionRequestDTO;
import com.example.jpa.dto.TransactionResponseDTO;
import com.example.jpa.model.IcesiAccount;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AccountMapper {

    //@Mapping(target = "user", source = "user", ignore = true)
    IcesiAccount fromAccountDTO(AccountRequestDTO createdAccountDTO);
    AccountResponseDTO fromAccountToSendAccountDTO(IcesiAccount account);
    TransactionResponseDTO fromTransactionRequest(TransactionRequestDTO transactionRequestDTO, String status);
}
