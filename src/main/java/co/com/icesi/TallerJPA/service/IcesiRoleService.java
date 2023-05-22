package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.error.exception.DetailBuilder;
import co.com.icesi.TallerJPA.error.exception.ErrorCode;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static co.com.icesi.TallerJPA.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository roleRepository;
    private final IcesiRoleMapper roleMapper;

    public IcesiRoleCreateDTO save(IcesiRoleCreateDTO roleDTO){
        roleValidation(roleDTO);
        verifyUserRole();
        IcesiRole role = roleMapper.fromIcesiRoleDTO(roleDTO);
        role.setRoleId(UUID.randomUUID());
        return roleMapper.fromIcesiRole(roleRepository.save(role));
    }
    private void roleValidation(IcesiRoleCreateDTO roleDTO){
        if(roleRepository.findByName(roleDTO.getName()).isPresent()){
            throw createIcesiException(
                    "Role name already exists in the database: "+ roleDTO.getName(),
                    HttpStatus.NOT_ACCEPTABLE,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Role",roleDTO.getName())).get();
        }
    }
    private void verifyUserRole(){
        String role = IcesiSecurityContext.getCurrentUserRole();
        if (!role.equals("ADMIN")){
            throw createIcesiException(
                    "This user does not have authorization",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }
}
