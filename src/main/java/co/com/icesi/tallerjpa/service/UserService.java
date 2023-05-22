package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.config.PasswordEncoderConfiguration;
import co.com.icesi.tallerjpa.config.SecurityConfiguration;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.error.util.IcesiExceptionBuilder;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final IcesiExceptionBuilder exceptionBuilder = new IcesiExceptionBuilder();
    private final PasswordEncoderConfiguration encoder = new PasswordEncoderConfiguration();

    public ResponseUserDTO save(RequestUserDTO requestUserDTO) {
        checkCreateUserPermission(requestUserDTO);

        Optional<IcesiUser> checkEmail = userRepository.findByEmail(requestUserDTO.getEmail());
        Optional<IcesiUser> checkPhone = userRepository.findByPhone(requestUserDTO.getPhoneNumber());

        StringBuilder errorMsg = new StringBuilder();
        checkEmail.ifPresent(e -> errorMsg.append("Email is already in use. "));
        checkPhone.ifPresent(e -> errorMsg.append("Phone is already in use."));

        if (errorMsg.length() > 0) {
            throw new RuntimeException(errorMsg.toString());
        }
        var checkRole = roleRepository.findByName(requestUserDTO.getRole())
                .orElseThrow(() -> exceptionBuilder.notFoundException("Role does not exist", "Role", "name", requestUserDTO.getRole()));

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(requestUserDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(checkRole);

        return userMapper.fromUserToRespUserDTO(userRepository.save(icesiUser));
    }

    public ResponseUserDTO getUser(String email) {
        var userExists = userRepository.findByEmail(email).isPresent();
        if (userExists) {
            return userMapper.fromUserToRespUserDTO(userRepository.findByEmail(email).get());
        }
        throw exceptionBuilder.notFoundException("User does not exist", "User", "email", email);
    }

    public List<ResponseUserDTO> getAllUsers() {
        List<IcesiUser> users = userRepository.findAll();
        return users.stream().map(userMapper::fromUserToRespUserDTO).collect(Collectors.toList());
    }

    public void checkCreateUserPermission(RequestUserDTO user) {
        String roleToken = IcesiSecurityContext.getCurrentRole();

        if(user.getRole().equals("ADMIN") && roleToken.equals("BANK") || roleToken.equals("USER")) {
            throw exceptionBuilder.forbiddenException("You can't create an admin user");
        }

    }

}


