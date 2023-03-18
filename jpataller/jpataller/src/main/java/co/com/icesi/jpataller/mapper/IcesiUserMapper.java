package co.com.icesi.jpataller.mapper;

import co.com.icesi.jpataller.dto.IcesiUserDTO;
import co.com.icesi.jpataller.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUser fromDTO(IcesiUserDTO userCreateDTO);

    IcesiUserDTO fromModel(IcesiUser icesiUser);
}
