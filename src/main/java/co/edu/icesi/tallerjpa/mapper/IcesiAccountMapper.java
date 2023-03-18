package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {

    IcesiAccount toIcesiAccount(IcesiAccountDTO icesiAccountDTO);

    IcesiAccountDTO toIcesiAccountDTO(IcesiAccount icesiAccount);
}
