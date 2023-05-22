package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.IcesiRoleDTO;
import icesi.university.accountSystem.dto.RequestUserDTO;
import icesi.university.accountSystem.dto.ResponseUserDTO;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-21T22:08:13-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
@Component
public class IcesiUserMapperImpl implements IcesiUserMapper {

    @Override
    public IcesiUser fromIcesiUserDTO(RequestUserDTO icesiUserDTO) {
        if ( icesiUserDTO == null ) {
            return null;
        }

        IcesiUser.IcesiUserBuilder icesiUser = IcesiUser.builder();

        icesiUser.firstName( icesiUserDTO.getFirstName() );
        icesiUser.lastName( icesiUserDTO.getLastName() );
        icesiUser.email( icesiUserDTO.getEmail() );
        icesiUser.phoneNumber( icesiUserDTO.getPhoneNumber() );
        icesiUser.password( icesiUserDTO.getPassword() );

        return icesiUser.build();
    }

    @Override
    public RequestUserDTO fromIcesiUser(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        RequestUserDTO.RequestUserDTOBuilder requestUserDTO = RequestUserDTO.builder();

        requestUserDTO.firstName( icesiUser.getFirstName() );
        requestUserDTO.lastName( icesiUser.getLastName() );
        requestUserDTO.email( icesiUser.getEmail() );
        requestUserDTO.password( icesiUser.getPassword() );
        requestUserDTO.phoneNumber( icesiUser.getPhoneNumber() );

        return requestUserDTO.build();
    }

    @Override
    public ResponseUserDTO fromUserToSendUserDTO(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        ResponseUserDTO.ResponseUserDTOBuilder responseUserDTO = ResponseUserDTO.builder();

        responseUserDTO.firstName( icesiUser.getFirstName() );
        responseUserDTO.lastName( icesiUser.getLastName() );
        responseUserDTO.email( icesiUser.getEmail() );
        responseUserDTO.password( icesiUser.getPassword() );
        responseUserDTO.phoneNumber( icesiUser.getPhoneNumber() );
        responseUserDTO.role( icesiRoleToIcesiRoleDTO( icesiUser.getRole() ) );

        return responseUserDTO.build();
    }

    @Override
    public List<ResponseUserDTO> fromUsersToSendUsersDTO(List<IcesiUser> users) {
        if ( users == null ) {
            return null;
        }

        List<ResponseUserDTO> list = new ArrayList<ResponseUserDTO>( users.size() );
        for ( IcesiUser icesiUser : users ) {
            list.add( fromUserToSendUserDTO( icesiUser ) );
        }

        return list;
    }

    protected IcesiRoleDTO icesiRoleToIcesiRoleDTO(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        IcesiRoleDTO.IcesiRoleDTOBuilder icesiRoleDTO = IcesiRoleDTO.builder();

        icesiRoleDTO.roleId( icesiRole.getRoleId() );
        icesiRoleDTO.description( icesiRole.getDescription() );
        icesiRoleDTO.name( icesiRole.getName() );
        List<IcesiUser> list = icesiRole.getIcesiUsers();
        if ( list != null ) {
            icesiRoleDTO.icesiUsers( new ArrayList<IcesiUser>( list ) );
        }

        return icesiRoleDTO.build();
    }
}
