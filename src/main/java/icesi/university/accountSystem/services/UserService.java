package icesi.university.accountSystem.services;

import icesi.university.accountSystem.dto.IcesiUserDTO;
import icesi.university.accountSystem.dto.RequestUserDTO;
import icesi.university.accountSystem.dto.ResponseUserDTO;
import icesi.university.accountSystem.exception.ExistsException;
import icesi.university.accountSystem.mapper.IcesiUserMapper;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiRoleRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private IcesiUserRepository icesiUserRepository;

    private IcesiUserMapper icesiUserMapper;

    private final IcesiRoleRepository roleRepository;
    @SneakyThrows
    public ResponseUserDTO save(RequestUserDTO userDTO){
        boolean emailExists = icesiUserRepository.existsByEmail(userDTO.getEmail());
        boolean phoneExists = icesiUserRepository.existsByPhoneNumber(userDTO.getPhoneNumber());

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
        IcesiUser user = icesiUserMapper.fromIcesiUserDTO(userDTO);
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        user.setUserId(UUID.randomUUID());
        var role = roleRepository.findByName(userDTO.getRole()).orElseThrow(() -> new ExistsException("Role doesn't exists"));
        user.setRole(role);

        return icesiUserMapper.fromUserToSendUserDTO(icesiUserRepository.save(user));
    }

    public ResponseUserDTO getUser(String userEmail) {
        Optional<IcesiUser> user = icesiUserRepository.findByEmail(userEmail);
        if(user.isPresent()){
            return icesiUserMapper.fromUserToSendUserDTO(user.get());
        }else{
            throw new RuntimeException("User doesn't exists");
        }
    }

    public List<ResponseUserDTO> getAllUsers() {
        List<IcesiUser> users = icesiUserRepository.findAll();
        return icesiUserMapper.fromUsersToSendUsersDTO(users);
    }
}
