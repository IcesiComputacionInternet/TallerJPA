package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.exception.*;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {

    private final IcesiUserRepository icesiUserRepository;

    private final IcesiUserMapper icesiUserMapper;

    private final IcesiRoleRepository icesiRoleRepository;

    private final IcesiRoleService icesiRoleService;

    private final IcesiAccountRepository icesiAccountRepository;

    public IcesiUser saveNewUser(IcesiUserDTO icesiUserDTO) throws DuplicateDataException, MissingParameterException {
        IcesiUser icesiUser = icesiUserMapper.toIcesiUser(icesiUserDTO);

        if (icesiUserDTO.getEmail() == null || icesiUserDTO.getEmail().isEmpty()
                || icesiUserDTO.getPhoneNumber() == null || icesiUserDTO.getPhoneNumber().isEmpty()
                || icesiUserDTO.getRoleName() == null || icesiUserDTO.getRoleName().isEmpty()) {
            throw new MissingParameterException("Email, phone number, and role name are required");
        }

        if (icesiUserRepository.findByEmail(icesiUserDTO.getEmail()).isPresent()) {
            throw new DuplicateDataException("Email already exists");
        }
        if (icesiUserRepository.findByPhoneNumber(icesiUserDTO.getPhoneNumber()).isPresent()) {
            throw new DuplicateDataException("Phone number already exists");
        }

        IcesiRole icesiRole = icesiRoleRepository.findByName(icesiUserDTO.getRoleName()).orElse(null);
        if (icesiRole == null) {
            throw new MissingParameterException("Role not found");
        }

        icesiUser.setIcesirole(icesiRole);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUserRepository.save(icesiUser);
        return icesiUser;
    }

    private void validateUserEmail(IcesiUser icesiUser) throws DataAlreadyExist {
        String email = icesiUser.getEmail();
        boolean emailAlreadyExist = icesiUserRepository.existsByEmail(email);
        if (emailAlreadyExist) {
            throw new DataAlreadyExist(String.format("Email  already exists", email));
        }
    }

    private void validatePhoneNumber(IcesiUser icesiUser) throws DataAlreadyExist {
        String phoneNumber = icesiUser.getPhoneNumber();
        boolean phoneAlreadyExist = icesiUserRepository.existsByPhoneNumber(phoneNumber);
        if (phoneAlreadyExist) {
            throw new DataAlreadyExist(String.format("Phone number already exists", phoneNumber));
        }
    }


    private void validateEmailAndPhoneNumber(IcesiUser icesiUser) throws DataAlreadyExist {
        boolean emailAlreadyExist = icesiUserRepository.existsByEmail(icesiUser.getEmail());
        boolean phoneAlreadyExist = icesiUserRepository.existsByPhoneNumber(icesiUser.getPhoneNumber());
        if (emailAlreadyExist || phoneAlreadyExist) {
            throw new DataAlreadyExist("Email or phone number already exists");
        }
    }


    private void validateRole(IcesiUserDTO icesiUserDTO) throws ParameterRequired {
        String roleName = icesiUserDTO.getRoleName();
        if (roleName == null || roleName.isEmpty()) {
            throw new ParameterRequired("Role is required");
        }
        boolean roleAlreadyExist = icesiRoleRepository.existsByName(roleName);
        if (!roleAlreadyExist) {
            throw new ParameterRequired("Role does not exist");
        }
    }

}
