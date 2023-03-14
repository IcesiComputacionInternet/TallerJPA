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
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("User already exists");
        }
        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        return userRepository.save(icesiUser);
    }

    public Optional<IcesiUser> findById(UUID fromString){
        return userRepository.findById(fromString);
    }

    public Optional<IcesiUser> findByEmail(String fromString){
        return userRepository.findByEmail(fromString);
    }

}
