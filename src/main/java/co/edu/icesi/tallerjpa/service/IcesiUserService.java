package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserShowDTO;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiUserMapper icesiUserMapper;

    public IcesiUserShowDTO save(IcesiUserCreateDTO icesiUserCreateDTO){
        ArrayList<String> errors = new ArrayList<>();

        if(!isEmailUnique(icesiUserCreateDTO.getEmail())){
            errors.add("There is already a user with the email " + icesiUserCreateDTO.getEmail() + "\n");
        }

        if(!isPhoneNumberUnique(icesiUserCreateDTO.getPhoneNumber())){
            errors.add("There is already a user with the phone number " + icesiUserCreateDTO.getPhoneNumber() + "\n");
        }

        if(!errors.isEmpty()){
            throw new RuntimeException(errors.stream().reduce("", (errorMessage, error) -> errorMessage + error));
        }

        IcesiRole icesiRole = icesiRoleRepository.findByName(icesiUserCreateDTO.getIcesiRoleCreateDTO().getName())
                .orElseThrow(() -> new RuntimeException(("There is no role with that name")));

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
        IcesiRole icesiRole = icesiRoleRepository.findByName(name).orElseThrow(() -> new RuntimeException("There is no role with that name"));
        return icesiRole.getRoleId();
    }
}
