package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.RequestUserDTO;
import icesi.university.accountSystem.dto.ResponseUserDTO;
import icesi.university.accountSystem.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {

    @Mapping(target = "role", source = "role",ignore=true)
    IcesiUser fromIcesiUserDTO(RequestUserDTO icesiUserDTO);
    @Mapping(target = "role", source = "role",ignore=true)
    RequestUserDTO fromIcesiUser(IcesiUser icesiUser);
    ResponseUserDTO fromUserToSendUserDTO(IcesiUser icesiUser);
    List<ResponseUserDTO> fromUsersToSendUsersDTO(List<IcesiUser> users);
}
