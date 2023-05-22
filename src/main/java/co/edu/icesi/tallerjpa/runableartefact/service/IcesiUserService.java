package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.DataAlreadyExist;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.ParameterRequired;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAuthorities;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.runableartefact.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {

    private final IcesiUserRepository icesiUserRepository;
    private final IcesiRoleRepository icesiRoleRepository;
    private final AuthoritiesService authoritiesService;
    private final IcesiUserMapper icesiUserMapper;


    public String saveNewUser(IcesiUserDTO icesiUserDTO) throws DataAlreadyExist, ParameterRequired {
        IcesiUser icesiUser = icesiUserMapper.toIcesiUser(icesiUserDTO);

        validateEmailAndPhoneNumber(icesiUser);
        validateUserEmail(icesiUser);
        validatePhoneNumber(icesiUser);
        validateRole(icesiUserDTO);
        if (icesiUserDTO.getRoleName().equals("ADMIN")) authoritiesService.validateAuthorities("ADMIN");


        icesiUser.setRole(icesiRoleRepository.findByName(icesiUserDTO.getRoleName()).get());

        icesiUser.setUserId(UUID.randomUUID());
        icesiUserRepository.save(icesiUser);
        return "User saved";
    }

    public IcesiUserDTO updateIcesiUser(IcesiUserDTO icesiUserDTO) throws ParameterRequired, DataAlreadyExist {
        IcesiUser actualUser = icesiUserRepository.findById(UUID.fromString(IcesiSecurityContext.getCurrentUserId())).orElseThrow(() -> new ParameterRequired("User not found"));
        actualUser.setFirstName(icesiUserDTO.getFirstName());
        actualUser.setLastName(icesiUserDTO.getLastName());
        actualUser.setEmail(icesiUserDTO.getEmail());
        actualUser.setPhoneNumber(icesiUserDTO.getPhoneNumber());
        actualUser.setPassword(icesiUserDTO.getPassword());
        actualUser.setRole(icesiRoleRepository.findByName(icesiUserDTO.getRoleName()).get());

        return icesiUserMapper.toIcesiUserDTO(icesiUserRepository.save(actualUser));
    }

    public List<IcesiUser> getAllUsers(){
        return icesiUserRepository.findAll();
    }

    private void validateUserEmail(IcesiUser icesiUser) throws DataAlreadyExist {
        boolean emailAlreadyExist = icesiUserRepository.existsByEmail(icesiUser.getEmail());
        if(emailAlreadyExist) {throw new DataAlreadyExist("Email already exists");}
    }
    private void validatePhoneNumber(IcesiUser icesiUser) throws DataAlreadyExist{
        boolean phoneAlreadyExist = icesiUserRepository.existsByPhoneNumber(icesiUser.getPhoneNumber());
        if(phoneAlreadyExist) {throw new DataAlreadyExist("Phone number already exists");}
    }
    private void validateEmailAndPhoneNumber(IcesiUser icesiUser) throws DataAlreadyExist{
        boolean emailAlreadyExist = icesiUserRepository.existsByEmail(icesiUser.getEmail());
        boolean phoneAlreadyExist = icesiUserRepository.existsByPhoneNumber(icesiUser.getPhoneNumber());
        if(emailAlreadyExist && phoneAlreadyExist) {throw new DataAlreadyExist("Email and phone number already exists");}
    }
    private void validateRole(IcesiUserDTO icesiUserDTO) throws ParameterRequired{
        boolean roleAlreadyExist = icesiRoleRepository.existsByName(icesiUserDTO.getRoleName()) ;
        if(!roleAlreadyExist) {throw new ParameterRequired("Role is required");}
    }


}
