package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.IcesiRoleDTO;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-18T09:39:36-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
@Component
public class IcesiRoleMapperImpl implements IcesiRoleMapper {

    @Override
    public IcesiRole fromIcesiRoleDTO(IcesiRoleDTO icesiRoleDTO) {
        if ( icesiRoleDTO == null ) {
            return null;
        }

        IcesiRole.IcesiRoleBuilder icesiRole = IcesiRole.builder();

        icesiRole.roleId( icesiRoleDTO.getRoleId() );
        icesiRole.description( icesiRoleDTO.getDescription() );
        icesiRole.name( icesiRoleDTO.getName() );
        List<IcesiUser> list = icesiRoleDTO.getIcesiUsers();
        if ( list != null ) {
            icesiRole.icesiUsers( new ArrayList<IcesiUser>( list ) );
        }

        return icesiRole.build();
    }

    @Override
    public IcesiRoleDTO fromIcesiRole(IcesiRole icesiRole) {
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
