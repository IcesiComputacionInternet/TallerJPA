package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;
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

    public IcesiUser save(UserCreateDTO user)  throws Exception{
        if (findUsersByEmail(user) && findUsersByPhoneNumber(user)) {
            throw new Exception("Ambos campos ya existen");
        } else if (findUsersByEmail(user)) {
            throw new Exception("El correo ya existe");
        } else if (findUsersByPhoneNumber(user)) {
            throw new Exception("El numero de telefono ya existe");
        } else {
            IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
            icesiUser.setUserId(UUID.randomUUID());
            return userRepository.save(icesiUser);
        }
    }

    private boolean findUsersByEmail(UserCreateDTO user) {
        List<String> emails = userRepository.findByEmail();
        return emails.stream().anyMatch(user.getEmail()::equals);
    }

    private boolean findUsersByPhoneNumber(UserCreateDTO user) {
        List<String> emails = userRepository.findByPhoneNumber();
        return emails.stream().anyMatch(user.getPhoneNumber()::equals);
    }
}
