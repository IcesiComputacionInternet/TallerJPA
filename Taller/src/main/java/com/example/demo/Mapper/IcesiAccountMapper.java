package com.example.demo.Mapper;


import com.example.demo.DTO.IcesiAccountDTO;
import com.example.demo.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);
    IcesiAccount fromIcesiAccountDTO(IcesiAccountDTO icesiAccountDTO);
}
