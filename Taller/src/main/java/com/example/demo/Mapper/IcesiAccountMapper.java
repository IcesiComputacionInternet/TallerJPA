package com.example.demo.Mapper;


import com.example.demo.DTO.IcesiAccountDTO;
import com.example.demo.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);

    IcesiAccount fromIcesiAccountDTO(IcesiAccountDTO icesiAccountDTO);
}
