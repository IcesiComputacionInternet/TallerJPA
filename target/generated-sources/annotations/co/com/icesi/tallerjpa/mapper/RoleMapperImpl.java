package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import co.com.icesi.tallerjpa.model.IcesiRole;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-22T04:14:30-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public IcesiRole fromIcesiRoleDTO(RoleCreateDTO roleCreateDTO) {
        if ( roleCreateDTO == null ) {
            return null;
        }

        IcesiRole.IcesiRoleBuilder icesiRole = IcesiRole.builder();

        icesiRole.description( roleCreateDTO.getDescription() );
        icesiRole.name( roleCreateDTO.getName() );

        return icesiRole.build();
    }

    @Override
    public RoleCreateDTO fromIcesiRole(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        RoleCreateDTO.RoleCreateDTOBuilder roleCreateDTO = RoleCreateDTO.builder();

        roleCreateDTO.description( icesiRole.getDescription() );
        roleCreateDTO.name( icesiRole.getName() );

        return roleCreateDTO.build();
    }
}
