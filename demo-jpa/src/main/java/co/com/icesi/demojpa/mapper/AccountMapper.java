package co.com.icesi.demojpa.mapper;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    IcesiUser fromIcesiUserDTO(UserCreateDTO userCreateDTO);

    UserCreateDTO fromIcesiUser(IcesiUser icesiUser);
}
