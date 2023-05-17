package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.IcesiAccountDto;
import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.dto.TransactionOperationDto;
import co.edu.icesi.demo.dto.TransactionResultDto;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {

    IcesiAccount fromIcesiAccountDto(IcesiAccountDto icesiAccountDto);
    IcesiAccountDto fromIcesiAccount(IcesiAccount icesiAccount);

    TransactionResultDto fromTransactionOperationDto(TransactionOperationDto operationDto, String result);
}
