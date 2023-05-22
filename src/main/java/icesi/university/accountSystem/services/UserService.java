package icesi.university.accountSystem.services;

import icesi.university.accountSystem.dto.RequestUserDTO;
import icesi.university.accountSystem.dto.ResponseUserDTO;
import icesi.university.accountSystem.exception.CustomException;
import icesi.university.accountSystem.exception.ExistsException;
import icesi.university.accountSystem.mapper.IcesiUserMapper;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiRoleRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import icesi.university.accountSystem.security.IcesiSecurityContext;
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

       checkCreateUserADMIN(userDTO.getRole());

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

    private String currentRole(){
       return IcesiSecurityContext.getCurrentUserRole();
    }

    private void checkCreateUserADMIN(String roleOfUserToCreate) {
        String role = currentRole();
        if (role.equals("BANK") && roleOfUserToCreate.equals("ADMIN")) {
            throw new CustomException("You cant create a admin", "ERR-404");

        }
    }

    public ResponseUserDTO assignRole(String userMail,String roleName) {
        Optional<IcesiUser> user = icesiUserRepository.findByEmail(userMail);
        if(user.isPresent()){
            var role = roleRepository.findByName(roleName).orElseThrow(() -> new ExistsException("Role doesn't exists"));
            IcesiUser newRole = user.get();
            newRole.setRole(role);
            return icesiUserMapper.fromUserToSendUserDTO(icesiUserRepository.save(newRole));
        }else{
            throw new RuntimeException("User doesn't exists");
        }
    }

}
