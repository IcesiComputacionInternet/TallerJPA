package co.com.icesi.jpataller.service;

import co.com.icesi.jpataller.dto.IcesiUserDTO;
import co.com.icesi.jpataller.mapper.IcesiUserMapper;
import co.com.icesi.jpataller.model.IcesiAccount;
import co.com.icesi.jpataller.model.IcesiUser;
import co.com.icesi.jpataller.repository.IcesiAccountRepository;
import co.com.icesi.jpataller.repository.IcesiRoleRepository;
import co.com.icesi.jpataller.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;

    private final IcesiUserMapper icesiUserMapper;

    private final IcesiRoleRepository icesiRoleRepository;

    private final IcesiRoleService icesiRoleService;

    private final IcesiAccountRepository icesiAccountRepository;

    public IcesiUser        createUser(IcesiUserDTO user) {
        boolean[] checks = {false, false, false};
        checks[0] = icesiUserRepository.findByEmail(user.getEmail()).isPresent();
        checks[1] = icesiUserRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent();
        // We don't want strings made of white spaces.
        checks[2] = user.getRoleName().isBlank();


        if (icesiUserRepository.findByEmail(user.getEmail()).isPresent() && icesiUserRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con este email y número de celular");
        } else if(icesiUserRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con este email");
        } else if (icesiUserRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con este número de celular");
        } else if (user.getRoleName().isBlank()) {
            throw new RuntimeException("No se puede crear un usuario sin rol");
        // Perhaps we can check if the roleName already exists
        }
        IcesiUser icesiUser = icesiUserMapper.fromDTO(user);
        icesiUser.setUserId(UUID.randomUUID());


        // We need to add the user to the rold
        icesiRoleService.addUserToRole(icesiRoleRepository.findByName(user.getRoleName()).get(), icesiUser.getUserId());
        icesiUser.setRole(icesiRoleRepository.findByName(user.getRoleName()).get());
        return icesiUserRepository.save(icesiUser);
    }

    public void createAccount(IcesiUserDTO icesiUserDTO, String accountNumber) {
        Optional<IcesiAccount> icesiAccount = icesiAccountRepository.findByAccountNumber(accountNumber);
        if (icesiAccount.isEmpty()) {
            throw new RuntimeException("No existe una cuenta con ese número");
        }
        IcesiUser icesiUser = icesiUserMapper.fromDTO(icesiUserDTO);
        icesiUser.getAccounts().add(icesiAccount.get());
    }

}
