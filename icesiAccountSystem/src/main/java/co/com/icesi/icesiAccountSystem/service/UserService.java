package co.com.icesi.icesiAccountSystem.service;

import co.com.icesi.icesiAccountSystem.dto.RoleDTO;
import co.com.icesi.icesiAccountSystem.dto.UserDTO;
import co.com.icesi.icesiAccountSystem.mapper.UserMapper;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public IcesiUser saveUser(UserDTO userDTO) {
        boolean foundSameEmail = userRepository.findByEmail(userDTO.getEmail()).isPresent();
        boolean foundSamePhone = userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent();
        if(foundSameEmail && foundSamePhone){
            throw new RuntimeException("Another user already has this email and phone number.");
        }else if (foundSameEmail) {
            throw new RuntimeException("Another user already has this email.");
        }else if (foundSamePhone){
            throw new RuntimeException("Another user already has this phone number.");
        }
        IcesiUser icesiUser = userMapper.fromUserDTO(userDTO);
        icesiUser.setUserId(UUID.randomUUID());
        assignUser(userDTO, icesiUser);

        return userRepository.save(icesiUser);
    }

    private void assignUser(UserDTO userDTO, IcesiUser user) {
        if (userDTO.getRoleName().equals("")){
            throw new RuntimeException("It is not possible to create a user without role.");
        }
        if(roleRepository.findByName(userDTO.getRoleName()).isPresent()){
            user.setRole(roleRepository.findByName(userDTO.getRoleName()).get());
        } else{
            throw new RuntimeException("Role does not exists.");
        }
    }
}
