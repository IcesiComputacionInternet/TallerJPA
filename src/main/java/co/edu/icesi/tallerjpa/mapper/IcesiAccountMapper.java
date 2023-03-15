package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.CreateIcesiAccountDTO;
import co.edu.icesi.tallerjpa.dto.ShowIcesiAccountDTO;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromCreateIcesiAccountDTO(CreateIcesiAccountDTO createIcesiAccountDTO);
    ShowIcesiAccountDTO fromIcesiAccountToShowDTO(IcesiAccount icesiAccount);
}
