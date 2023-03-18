package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.UserCreateDTO;
import co.edu.icesi.demo.mapper.RoleMapper;
import co.edu.icesi.demo.mapper.UserMapper;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.RoleRepository;
import co.edu.icesi.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    private RoleRepository roleRepository;

    public IcesiUser save(UserCreateDTO user){

        validateEmailAndPhoneNumber(user);
        validateNullRole(user);
        IcesiRole icesiRole=roleRepository.findByName(user.getRoleCreateDTO().getName()).orElseThrow( ()-> new RuntimeException("User role does not exists"));
        IcesiUser icesiUser=userMapper.fromIcesiUserDTO(user);
        icesiUser.setRole(icesiRole);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setAccounts(new ArrayList<>());
        icesiRole.getUsers().add(icesiUser);
        return userRepository.save(icesiUser);

    }

    public void validateEmailAndPhoneNumber(UserCreateDTO user){
        if(userRepository.findByEmail(user.getEmail()).isPresent() && userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            throw new RuntimeException("User email and phone number are in use");
        }else if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("User email is in use");
        }else if(userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            throw new RuntimeException("User phone number is in use");
        }
    }

    public void validateNullRole(UserCreateDTO user){
        if(user.getRoleCreateDTO()==null){
            throw new RuntimeException("User needs a role");
        }
    }


}
