package co.com.icesi.demojpa.servicio;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.ResponseUserDTO;
import co.com.icesi.demojpa.error.util.IcesiExceptionBuilder;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.mapper.response.UserResponseMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    private final AccountRepository accountRepository;

    private final UserResponseMapper userResponseMapper;


    public ResponseUserDTO save(UserCreateDTO user){

        if(user.getRoleName().isEmpty()){
            throw new RuntimeException("El usuario no tiene rol");
        }

        System.out.println("El rol del usuario es: "+user.getRoleName());
        IcesiRole role = roleRepository.findByName(user.getRoleName()).orElseThrow(
                ()-> IcesiExceptionBuilder.createIcesiException("No existe un rol con este nombre", HttpStatus.NOT_FOUND,"ROLE_NOT_FOUND") );

        if(userRepository.findByEmail(user.getEmail()).isPresent() && userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new RuntimeException("Ya hay un usuario con este email y celular");
        }

        if(userRepository.findByPhone(user.getPhone()).isPresent()){
            throw new RuntimeException("Ya hay un usuario con este celular");
        }

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Ya hay un usuario con este email");
        }

        if(role.getName().equals("ADMIN") && !IcesiSecurityContext.getCurrentUserRole().equals("ADMIN")){
            throw new RuntimeException("Solo un usuario con rol ADMIN puede crear un usuario con rol ADMIN");
        }

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(role);
        userRepository.save(icesiUser);
        roleService.addUser(role,icesiUser.getUserId());


        return userResponseMapper.fromIcesUser(icesiUser);
    }


    public void addAccount(IcesiUser icesiUser,String accountNumber){

        IcesiAccount icesiAccount = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                ()-> IcesiExceptionBuilder.createIcesiException("No existe una cuenta con este numero", HttpStatus.NOT_FOUND,"ACCOUNT_NOT_FOUND"));

        icesiUser.getAccounts().add(icesiAccount);

    }


    public Optional<IcesiUser> findById(UUID fromString){
        return userRepository.findById(fromString);
    }

}
