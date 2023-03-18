package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.UserCreateDTO;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public IcesiUser save(UserCreateDTO user) {
        checkRole(user);
        if (userRepository.findByEmail(user.getEmail()).isPresent() && userRepository.findByPhone(user.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Email and Phone are already in use");
        } else if (userRepository.findByPhone(user.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Phone is already in use");
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }


            IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
            icesiUser.setUserId(UUID.randomUUID());
            return userRepository.save(icesiUser);
    }

    public void checkRole(UserCreateDTO user){
        if(user.getIcesiroleDto() == null){
            throw new RuntimeException("This role does not exist");
        }
    }
}
