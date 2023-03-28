package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.ResponseIcesiAccountDTO;
import com.example.demo.model.IcesiAccount;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    
    IcesiAccount fromIcesiAccountDTO(IcesiAccountCreateDTO IcesiAccountCreateDTO);
    IcesiAccountCreateDTO fromIcesiAccount(IcesiAccount icesiAccount);
    ResponseIcesiAccountDTO fromIcesiAcountToIcesiAccountDTO(IcesiAccount icesiAccount);
}
