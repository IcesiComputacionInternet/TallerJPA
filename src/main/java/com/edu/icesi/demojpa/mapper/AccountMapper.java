package com.edu.icesi.demojpa.mapper;

import com.edu.icesi.demojpa.dto.request.RequestAccountDTO;
import com.edu.icesi.demojpa.dto.request.RequestTransactionDTO;
import com.edu.icesi.demojpa.dto.response.ResponseAccountDTO;
import com.edu.icesi.demojpa.dto.response.ResponseTransactionDTO;
import com.edu.icesi.demojpa.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface AccountMapper {
    @Mapping(target = "icesiUser", source = "icesiUserId", ignore = true)
    IcesiAccount fromIcesiAccountDTO(RequestAccountDTO requestAccountDTO);

    @Mapping(target = "result", source = "result")
    ResponseTransactionDTO fromTransactionDTO(RequestTransactionDTO transactionDTO, String result);

    @Mapping(target = "result", source = "result")
    @Mapping(target = "userId", expression = "java(icesiAccount.getIcesiUser().getUserId())")
    ResponseAccountDTO fromAccountDTO(IcesiAccount icesiAccount, String result);

    @Mapping(target = "userId", expression = "java(icesiAccount.getIcesiUser().getUserId())")
    ResponseAccountDTO fromAccountToDTO(IcesiAccount icesiAccount);
}
