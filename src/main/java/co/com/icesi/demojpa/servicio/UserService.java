package co.com.icesi.demojpa.servicio;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    private final AccountRepository accountRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleService roleService, RoleRepository roleRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
    }

    public IcesiUser save(UserCreateDTO user){
        if(userRepository.findByEmail(user.getEmail()).isPresent() && userRepository.findByPhone(user.getPhone()).isPresent()){
            throw new RuntimeException("Ya hay un usuario con este email y celular");
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Ya hay un usuario con este email");
        } else if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new RuntimeException("Ya hay un usuario con este celular");
        } else if (user.getRoleName().isEmpty()) {
            throw new RuntimeException("El usuario no tiene rol");
        } else if (!roleRepository.findByName(user.getRoleName()).isPresent()) {
            throw new RuntimeException("Este rol no existe");
        }
        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        roleService.addUser(roleRepository.findByName(user.getRoleName()).get(),icesiUser.getUserId());
        icesiUser.setRole(roleRepository.findByName(user.getRoleName()).get());

        return userRepository.save(icesiUser);
    }


    public void addAccount(IcesiUser icesiUser,String accountNumber){
        if(accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
            throw new RuntimeException("No existe una cuenta con este numero");
        }
        icesiUser.getAccounts().add(accountRepository.findByAccountNumber(accountNumber).get());

    }


    public Optional<IcesiUser> findById(UUID fromString){
        return userRepository.findById(fromString);
    }

}
