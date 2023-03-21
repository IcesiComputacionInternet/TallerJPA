package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.exception.ExistsException;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @SneakyThrows
    public ResponseUserDTO save(RequestUserDTO userDTO) {

        boolean emailExists = userRepository.existsByEmail(userDTO.getEmail());
        boolean phoneExists = userRepository.existsByPhoneNumber(userDTO.getPhoneNumber());

        List<String> errors = new ArrayList<>();

        if (emailExists) {
            errors.add("Email already exists");
        }
        if (phoneExists) {
            errors.add("Phone number already exists");
        }

        if(!errors.isEmpty()){
            throw new ExistsException(String.join(" ", errors));
        }

        IcesiUser user = userMapper.fromUserDTO(userDTO);
        user.setUserId(UUID.randomUUID());
        var role = roleRepository.findByName(userDTO.getRole()).orElseThrow(() -> new ExistsException("Role doesn't exists"));
        user.setRole(role);

        return userMapper.fromUserToSendUserDTO(userRepository.save(user));

    }
}
