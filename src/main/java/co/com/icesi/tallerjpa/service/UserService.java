package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
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

    public ResponseUserDTO save(RequestUserDTO requestUserDTO) {

        Optional<IcesiUser> checkEmail = userRepository.findByEmail(requestUserDTO.getEmail());
        Optional<IcesiUser> checkPhone = userRepository.findByPhone(requestUserDTO.getPhoneNumber());

        StringBuilder errorMsg = new StringBuilder();
        checkEmail.ifPresent(e -> errorMsg.append("Email is already in use. "));
        checkPhone.ifPresent(e -> errorMsg.append("Phone is already in use."));

        if (errorMsg.length() > 0) {
            throw new RuntimeException(errorMsg.toString());
        }
        var checkRole = roleRepository.findByName(requestUserDTO.getRole())
                .orElseThrow(() -> new RuntimeException("This role does not exist"));


        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(requestUserDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(checkRole);

        return userMapper.fromUserToRespUserDTO(userRepository.save(icesiUser));
    }

    public ResponseUserDTO getUser(String email){
        var userExists = userRepository.findByEmail(email).isPresent();
        if(userExists){
            return userMapper.fromUserToRespUserDTO(userRepository.findByEmail(email).get());
        }
        throw new RuntimeException("User does not exist");
    }

    public List<ResponseUserDTO> getAllUsers(){
        List<IcesiUser> users = userRepository.findAll();
        return users.stream().map(userMapper::fromUserToRespUserDTO).collect(Collectors.toList());
    }
}
