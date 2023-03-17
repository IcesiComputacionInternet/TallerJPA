package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.UserCreateDTO;
import co.edu.icesi.demo.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    IcesiUser fromIcesiUserDTO(UserCreateDTO userCreateDTO);

}
