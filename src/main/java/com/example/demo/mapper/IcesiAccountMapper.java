package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.DTO.ResponseTransactionDTO;
import com.example.demo.DTO.TransactionCreateDTO;
import com.example.demo.model.IcesiAccount;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    
    IcesiAccount fromIcesiAccountCreateDTO(IcesiAccountCreateDTO IcesiAccountCreateDTO);
    ResponseIcesiAccountDTO fromIcesiAccountToResponseIcesiAccountDTO(IcesiAccount icesiAccount);
    ResponseIcesiAccountDTO fromIcesiAccountCreateDTOToResponseIcesiAccountDTO(IcesiAccountCreateDTO icesiAccountCreateDTO);
    ResponseTransactionDTO fromTransactionCrateDTO(TransactionCreateDTO transactionCreateDTO);
}
