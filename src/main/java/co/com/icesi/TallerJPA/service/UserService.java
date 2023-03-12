package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.exception.ArgumentsException;
import co.com.icesi.TallerJPA.mapper.RoleMapper;
import co.com.icesi.TallerJPA.mapper.UserMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.RoleRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;


    public IcesiUser save(UserCreateDTO user) {
        boolean email = userRepository.findByEmail(user.getEmail());
        boolean phoneNumber = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if(email && phoneNumber){
            throw new ArgumentsException("Email and phone number already exist");
        }else if (email) {
            throw new ArgumentsException("Email already exist");
        }else if (phoneNumber) {
            throw new ArgumentsException("Phone number already exist");
        }

        boolean existRole = roleRepository.findByName(user.getRole().getName());
        if (!existRole) {
            throw new ArgumentsException("Role does not exist");
        }


        user.setRole(roleRepository.returnRole(user.getRole().getName()));
        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        return userRepository.save(icesiUser);
    }

    /*
    private boolean findUsersByEmail(UserCreateDTO user) {
        List<String> emails = userRepository.findByEmail();
        return emails.stream().anyMatch(user.getEmail()::equals);
    }

    private boolean findUsersByPhoneNumber(UserCreateDTO user) {
        List<String> emails = userRepository.findByPhoneNumber();
        return emails.stream().anyMatch(user.getPhoneNumber()::equals);
    }
     */
}
