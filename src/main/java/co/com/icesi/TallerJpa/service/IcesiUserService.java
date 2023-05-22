package co.com.icesi.TallerJpa.service;

import co.com.icesi.TallerJpa.dto.IcesiUserRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiUserResponseDTO;
import co.com.icesi.TallerJpa.dto.RoleChangeDTO;
import co.com.icesi.TallerJpa.error.exception.DetailBuilder;
import co.com.icesi.TallerJpa.error.exception.ErrorCode;
import co.com.icesi.TallerJpa.mapper.IcesiUserMapper;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiRoleRepository;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static co.com.icesi.TallerJpa.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiUserMapper icesiUserMapper;

    public IcesiUserResponseDTO saveUser(IcesiUserRequestDTO icesiUserRequestDTO, String actualUserRole){
        List<String> errors = new ArrayList<>();
        if(icesiUserRepository.findByEmail(icesiUserRequestDTO.getEmail()).isPresent()){
            errors.add("email");
        }
        if(icesiUserRepository.findByPhoneNumber(icesiUserRequestDTO.getPhoneNumber()).isPresent()){
            errors.add("phoneNumber");
        }
        if(!errors.isEmpty()){
            throw createIcesiException(
                    "Field duplicated",
                    HttpStatus.BAD_REQUEST,
                    errors.stream()
                            .map(message -> new DetailBuilder(ErrorCode.ERR_400,message,"is duplicated"))
                            .toArray(DetailBuilder[]::new)
            ).get();
        }
        IcesiRole icesiRole = privateGetRoleByName(icesiUserRequestDTO.getRole());
        checkPermissionsToCreateUser(actualUserRole, icesiRole.getName());

        IcesiUser icesiUser = icesiUserMapper.fromUserDto(icesiUserRequestDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setIcesiRole(icesiRole);
        return icesiUserMapper.fromIcesiUserToResponse(icesiUserRepository.save(icesiUser));
    }

    public IcesiUserResponseDTO assignRole(RoleChangeDTO roleChangeDTO){
        IcesiUser icesiUser = privateGetUserByEmail(roleChangeDTO.getEmail());
        IcesiRole icesiRole = privateGetRoleByName(roleChangeDTO.getRole());
        icesiUser.setIcesiRole(icesiRole);
        return icesiUserMapper.fromIcesiUserToResponse(icesiUserRepository.save(icesiUser));
    }

    public IcesiUserResponseDTO getUserById(UUID idString){
        return icesiUserMapper.fromIcesiUserToResponse(privateGetUserById(idString));
    }

    public IcesiUserResponseDTO getUserByEmail(String email){
        return icesiUserMapper.fromIcesiUserToResponse(privateGetUserByEmail(email));
    }

    private void checkPermissionsToCreateUser(String actualUserRole, String role){
        boolean userIsNotAdmin = !actualUserRole.equals("ADMIN");
        boolean roleAssignIsAdmin = role.equals("ADMIN");
        if(userIsNotAdmin && roleAssignIsAdmin){
            throw createIcesiException(
                    "Forbidden",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403,"Forbidden. Only ADMINs can create ADMIN users")
            ).get();
        }
    }

    private IcesiUser privateGetUserById(UUID idString){
        return icesiUserRepository.findById(idString)
                .orElseThrow(createIcesiException(
                        "User not found",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404,"IcesiUser","userId",idString)
                ));
    }
    private IcesiUser privateGetUserByEmail(String email){
        return icesiUserRepository.findByEmail(email)
                .orElseThrow(createIcesiException(
                        "User not found",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404,"IcesiUser","email",email)
                ));
    }
    private IcesiRole privateGetRoleByName(String name){
        return icesiRoleRepository.findByName(name)
                .orElseThrow(createIcesiException(
                        "Role: "+name+" not found",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404,"IcesiRole","name",name)
                ));
    }
}
