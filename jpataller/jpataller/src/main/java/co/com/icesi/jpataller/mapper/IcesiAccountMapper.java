package co.com.icesi.jpataller.mapper;

import co.com.icesi.jpataller.dto.IcesiAccountDTO;
import co.com.icesi.jpataller.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {

    IcesiAccount fromDTO(IcesiAccountDTO icesiAccountDTO);

    IcesiAccountDTO fromModel(IcesiAccount icesiAccount);
}
