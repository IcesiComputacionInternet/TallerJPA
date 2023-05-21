package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserShowDTO;
import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.error.exception.DetailBuilder;
import co.edu.icesi.tallerjpa.error.exception.ErrorCode;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import static co.edu.icesi.tallerjpa.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiUserMapper icesiUserMapper;
    private final PasswordEncoder passwordEncoder;

    public IcesiUserShowDTO save(IcesiUserCreateDTO icesiUserCreateDTO, String icesiUserRole){
        ArrayList<String> errors = new ArrayList<>();

        if(!isEmailUnique(icesiUserCreateDTO.getEmail())){
            errors.add("There is already a user with the email " + icesiUserCreateDTO.getEmail() + ". ");
        }

        if(!isPhoneNumberUnique(icesiUserCreateDTO.getPhoneNumber())){
            errors.add("There is already a user with the phone number " + icesiUserCreateDTO.getPhoneNumber() + ". ");
        }

        if(!errors.isEmpty()){
            String allErrorMessages = errors.stream().reduce("", (errorMessage, error) -> errorMessage + error);
            throw createIcesiException(
                    allErrorMessages,
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "email or phone number", allErrorMessages)
            ).get();
        }

        IcesiRole icesiRole = getIcesiRoleByName(icesiUserCreateDTO.getIcesiRoleCreateDTO().getName());
        checkPermissionsToCreateUser(icesiUserRole, icesiRole.getName());

        IcesiUser icesiUser = icesiUserMapper.fromCreateIcesiUserDTO(icesiUserCreateDTO);
        icesiUser.setIcesiRole(icesiRole);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setPassword(passwordEncoder.encode(icesiUserCreateDTO.getPassword()));
        return icesiUserMapper.fromIcesiUserToShow(icesiUserRepository.save(icesiUser));
    }
    private boolean isEmailUnique(String email){
        if(icesiUserRepository.findByEmail(email).isPresent()){
            return false;
        }
        return true;
    }

    private boolean isPhoneNumberUnique(String phoneNumber){
        if(icesiUserRepository.findByPhoneNumber(phoneNumber).isPresent()){
            return false;
        }
        return true;
    }

    public IcesiUserShowDTO editRole(String icesiUserId, String roleName){
        IcesiUser icesiUser = getIcesiUserById(icesiUserId);
        IcesiRole icesiRole = getIcesiRoleByName(roleName);
        icesiUser.setIcesiRole(icesiRole);
        return icesiUserMapper.fromIcesiUserToShow(icesiUserRepository.save(icesiUser));
    }

    private IcesiRole getIcesiRoleByName(String roleName){
        return icesiRoleRepository.findByName(roleName)
                .orElseThrow(createIcesiException(
                        "There is no role with that name",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "role", "name", roleName)
                ));
    }

    private IcesiUser getIcesiUserById(String icesiUserId){
        return icesiUserRepository.findById(fromIdToUUID(icesiUserId)).orElseThrow(
                createIcesiException(
                        "There is no icesi user with the id: " + icesiUserId,
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "icesi user", "id", icesiUserId))
        );
    }

    private UUID fromIdToUUID(String icesiUserId){
        try{
            return UUID.fromString(icesiUserId);
        }catch (IllegalArgumentException illegalArgumentException){
            throw createIcesiException(
                    "Invalid account id",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "id", "Invalid account id")
            ).get();
        }
    }

    private void checkPermissionsToCreateUser(String icesiUserRole, String roleName){
        boolean userIsNotAnAdmin = !icesiUserRole.equals(NameIcesiRole.ADMIN.toString());
        boolean roleToAssignIsADMIN = roleName.equals(NameIcesiRole.ADMIN.toString());
        if (userIsNotAnAdmin && roleToAssignIsADMIN){
            throw createIcesiException(
                    "Forbidden",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403, "Forbidden. Only ADMIN users can create ADMIN users")
            ).get();
        }
    }
}
