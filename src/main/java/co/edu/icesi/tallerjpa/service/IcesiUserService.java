package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserShowDTO;
import co.edu.icesi.tallerjpa.error.exception.DetailBuilder;
import co.edu.icesi.tallerjpa.error.exception.ErrorCode;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public IcesiUserShowDTO save(IcesiUserCreateDTO icesiUserCreateDTO){
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

        IcesiRole icesiRole = icesiRoleRepository.findByName(icesiUserCreateDTO.getIcesiRoleCreateDTO().getName())
                .orElseThrow(createIcesiException(
                        "There is no role with that name",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "role", "name", icesiUserCreateDTO.getIcesiRoleCreateDTO().getName())
                ));

        IcesiUser icesiUser = icesiUserMapper.fromCreateIcesiUserDTO(icesiUserCreateDTO);
        icesiUser.setIcesiRole(icesiRole);
        icesiUser.setUserId(UUID.randomUUID());

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

    private UUID getUUIDOfRoleByName(String name){
        IcesiRole icesiRole = icesiRoleRepository.findByName(name)
                .orElseThrow(createIcesiException(
                        "There is no role with that name",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "role", "name", name)
                ));
        return icesiRole.getRoleId();
    }
}
