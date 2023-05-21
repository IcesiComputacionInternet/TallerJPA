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
    IcesiAccount fromIcesiAccountDTO(RequestAccountDTO requestAccountDTO);

    @Mapping(target = "result", source = "result")
    ResponseTransactionDTO fromTransactionDTO(RequestTransactionDTO transactionDTO, String result);

    @Mapping(target = "result", source = "result")
    ResponseAccountDTO fromAccountDTO(IcesiAccount icesiAccount, String result);

    ResponseAccountDTO fromAccountToDTO(IcesiAccount icesiAccount);
}
