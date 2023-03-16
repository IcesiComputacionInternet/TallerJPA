package co.com.icesi.demojpa.servicio;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public IcesiUser save(UserCreateDTO user){
        if(userRepository.findByEmail(user.getEmail()).isPresent() && userRepository.findByPhone(user.getPhone()).isPresent()){
            throw new RuntimeException("Ya hay un usuario con este email y celular");
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Ya hay un usuario con este email");
        } else if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new RuntimeException("Ya hay un usuario con este celular");
        } else if (user.getRole()==null) {
            throw new RuntimeException("El usuario no tiene rol");
            //TODO verificar que esto funcione porque no me fio la verdad
        }
        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        return userRepository.save(icesiUser);
    }

    public Optional<IcesiUser> findById(UUID fromString){
        return userRepository.findById(fromString);
    }

    public Optional<IcesiUser> findByPhone(String fromString){
        return userRepository.findByPhone(fromString);
    }

    public Optional<IcesiUser> findByEmail(String fromString){
        return userRepository.findByEmail(fromString);
    }

}
