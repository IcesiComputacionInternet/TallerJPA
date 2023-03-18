package com.example.demo.Mapper;


import com.example.demo.DTO.IcesiAccountDTO;
import com.example.demo.model.IcesiAccount;

public interface IcesiAccountMapper {
    IcesiAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);
    IcesiAccount fromIcesiAccountDTO(IcesiAccountDTO icesiAccountDTO);
}
