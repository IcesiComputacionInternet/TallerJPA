package co.edu.icesi.tallerJPA.mapper;

import co.edu.icesi.tallerJPA.dto.IcesiUserDTO;
import co.edu.icesi.tallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUser fromIcesiUserDTO(IcesiUserDTO icesiUserDTO);
    IcesiUserDTO fromIcesiUser(IcesiUser icesiUser);


}
