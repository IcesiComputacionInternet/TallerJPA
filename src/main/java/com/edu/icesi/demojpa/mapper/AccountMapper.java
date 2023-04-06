package com.edu.icesi.demojpa.mapper;

import com.edu.icesi.demojpa.dto.RequestAccountDTO;
import com.edu.icesi.demojpa.dto.RequestTransactionDTO;
import com.edu.icesi.demojpa.dto.ResponseAccountDTO;
import com.edu.icesi.demojpa.dto.ResponseTransactionDTO;
import com.edu.icesi.demojpa.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    IcesiAccount fromIcesiAccountDTO(RequestAccountDTO requestAccountDTO);

    @Mapping(target = "result", source = "result")
    ResponseTransactionDTO fromTransactionDTO(RequestTransactionDTO transactionDTO, String result);

    @Mapping(target = "result", source = "result")
    ResponseAccountDTO fromAccountDTO(IcesiAccount icesiAccount, String result);
}
