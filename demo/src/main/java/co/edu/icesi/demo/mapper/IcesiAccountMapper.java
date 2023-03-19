package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.IcesiAccountDto;
import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper
public interface IcesiAccountMapper {

    IcesiAccount fromIcesiAccountDto(IcesiAccountDto icesiAccountDto);
    IcesiAccountDto fromIcesiAccount(IcesiAccount icesiAccount);
}
