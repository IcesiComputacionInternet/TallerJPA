package co.com.icesi.demojpa.servicio;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiRole;
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

        if(user.getRoleName().isEmpty()){
            throw new RuntimeException("El usuario no tiene rol");
        }

        IcesiRole role = roleRepository.findByName(user.getRoleName()).orElseThrow(()->new RuntimeException("Este rol no existe"));

        if(userRepository.findByEmail(user.getEmail()).isPresent() && userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new RuntimeException("Ya hay un usuario con este email y celular");
        }

        if(userRepository.findByPhone(user.getPhone()).isPresent()){
            throw new RuntimeException("Ya hay un usuario con este celular");
        }

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Ya hay un usuario con este email");
        }

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        roleService.addUser(role,icesiUser.getUserId());
        icesiUser.setRole(role);

        return userRepository.save(icesiUser);
    }


    public void addAccount(IcesiUser icesiUser,String accountNumber){

        IcesiAccount icesiAccount = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("No existe una cuenta con este numero"));

        icesiUser.getAccounts().add(icesiAccount);

    }


    public Optional<IcesiUser> findById(UUID fromString){
        return userRepository.findById(fromString);
    }

}
