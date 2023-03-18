package co.edu.icesi.tallerJPA.mapper;

import co.edu.icesi.tallerJPA.dto.IcesiAccountCreateDTO;
import co.edu.icesi.tallerJPA.dto.IcesiAccountToShowDTO;
import co.edu.icesi.tallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromIcesiAccountCreateDTO(IcesiAccountCreateDTO icesiAccountCreateDTO);
    IcesiAccountToShowDTO fromIcesiAccountToShowDTO(IcesiAccount icesiAccount);
}
