package co.edu.icesi.tallerJPA.service;

import co.edu.icesi.tallerJPA.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerJPA.mapper.IcesiUserMapper;
import co.edu.icesi.tallerJPA.model.IcesiRole;
import co.edu.icesi.tallerJPA.model.IcesiUser;
import co.edu.icesi.tallerJPA.repository.IcesiRoleRepository;
import co.edu.icesi.tallerJPA.repository.IcesiUserRepository;
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

    public IcesiUser save(IcesiUserCreateDTO user){
        checkEmailAndPhoneNumber(user);
        checkExistingRole(user);
        IcesiRole icesiRole = icesiRoleRepository.findByName(user.getIcesiRoleCreateDTO().getName()).
        orElseThrow(()-> new RuntimeException("This user role not exist, please register it or use other"));
        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserCreateDTO(user);
        icesiUser.setRole(icesiRole);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setAccounts(new ArrayList<>());
        icesiRole.getUsers().add(icesiUser);
        return icesiUserRepository.save(icesiUser);
    }

    private void checkExistingRole(IcesiUserCreateDTO user) {
        if (user.getIcesiRoleCreateDTO()==null){
            throw new RuntimeException("Please register a role");
        }
    }

    private void checkEmailAndPhoneNumber(IcesiUserCreateDTO user) {
        if (icesiUserRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("The user email are registered");
        }else  if (icesiUserRepository.findByPhoneNumber(user.getPhone()).isPresent()){
            throw new RuntimeException("The user phone number are registered");
        }else  if (icesiUserRepository.findByEmail(user.getEmail()).isPresent() &&
                icesiUserRepository.findByPhoneNumber(user.getPhone()).isPresent()){
            throw new RuntimeException("The user email and  phone number are registered");
        }
    }
}
