package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.response.IcesiUserResponseDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {

    IcesiUser toIcesiUser(IcesiUserDTO icesiUserDTO);

    IcesiUserDTO toIcesiUserDTO(IcesiUser icesiUser);

    IcesiUserDTO toIcesiUserDTO(IcesiUserResponseDTO icesiUserResponseDTO);

    IcesiUserResponseDTO toIcesiUserResponseDTO(IcesiUser icesiUser);

    IcesiUserResponseDTO toIcesiUserResponseDTO(IcesiUserDTO icesiUserDTO);

}
