package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.IcesiAccountResponseDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {

    IcesiAccount toIcesiAccount(IcesiAccountDTO icesiAccountDTO);

    IcesiAccountDTO toIcesiAccountDTO(IcesiAccount icesiAccount);

    IcesiAccountDTO toIcesiAccountDTO(IcesiAccountResponseDTO icesiAccountResponseDTO);

    IcesiAccountResponseDTO toIcesiAccountResponseDTO(IcesiAccount icesiAccount);

    IcesiAccountResponseDTO toIcesiAccountResponseDTO(IcesiAccountDTO icesiAccountDTO);
}
