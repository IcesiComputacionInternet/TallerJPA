package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.exception.MissingArgumentsException;
import co.com.icesi.TallerJPA.mapper.UserMapper;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public IcesiUser save(UserCreateDTO user) {

        boolean email = userRepository.findByEmail(user.getEmail());
        boolean phoneNumber = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if(email && phoneNumber){
            throw new MissingArgumentsException("Email and phone number already exist");
        }else if (email) {
            throw new MissingArgumentsException("Email already exist");
        }else if (phoneNumber) {
            throw new MissingArgumentsException("Phone number already exist");
        }

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
