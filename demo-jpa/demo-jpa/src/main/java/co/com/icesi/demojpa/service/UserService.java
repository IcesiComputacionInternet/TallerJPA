package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import co.com.icesi.demojpa.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final AccountRepository accountRepository;

    public IcesiUser save(UserCreateDTO user){

        if(userRepository.findByEmail(user.getEmail()).isPresent() && userRepository.findByPhone(user.getPhone()).isPresent()){
            throw new RuntimeException("User phone and email are already in use");
        }else if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("User email is already in use");
        } else if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new RuntimeException("User phone is already in use");
        }


        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());

        if(roleRepository.findByName(user.getRoleName()).isPresent()){
            roleService.addUserToRole(roleRepository.findByName(user.getRoleName()).get(),icesiUser.getUserId());
            icesiUser.setRole(roleRepository.findByName(user.getRoleName()).get());
        }else{
            throw new RuntimeException("Role doesn't exists");
        }


        return userRepository.save(icesiUser);
    }

    public void addAccount(IcesiUser user, String accountNum){
        if(accountRepository.findByAccountNumber(accountNum).isPresent()){
            user.getAccounts().add(accountRepository.findByAccountNumber(accountNum).get());
        }else{
            throw new RuntimeException("Account doesn't exists");
        }
    }

    public Optional<IcesiUser> findById(UUID fromString){
        return userRepository.findById(fromString);
    }

    public Optional<IcesiUser> findByEmail(String fromString){
        return userRepository.findByEmail(fromString);
    }
}
