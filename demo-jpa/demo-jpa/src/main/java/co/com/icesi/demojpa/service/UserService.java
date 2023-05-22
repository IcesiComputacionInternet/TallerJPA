package co.com.icesi.demojpa.service;


import co.com.icesi.demojpa.config.PasswordEncoderConfiguration;
import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.UserResponseDTO;
import co.com.icesi.demojpa.error.util.IcesiExceptionBuilder;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository userRespository;

    private final RoleRepository roleRepository;

    private final IcesiExceptionBuilder eb = new IcesiExceptionBuilder();

    private final PasswordEncoderConfiguration encoder = new PasswordEncoderConfiguration();


    public UserResponseDTO save(UserCreateDTO user) {

        Boolean existEmail = userRespository.existsByEmail(user.getEmail());
        Boolean existPhone = userRespository.existsByPhone(user.getPhone());

        if(existEmail && existPhone){
            throw eb.exceptionDuplicate("Email and phone are repeated", "user", "email and phone", user.getEmail() + " y " + user.getPhone());}
        if(existEmail){
            throw eb.exceptionDuplicate("Email is repeated", "user", "email", user.getEmail());}
        if(existPhone){
            throw eb.exceptionDuplicate("Phone is repeated", "user", "phone", user.getPhone());}

        return createUser(user);
    }

    public UserResponseDTO createUser(UserCreateDTO user) {
        IcesiUser icesiUser = userMapper.fromIcesiUser(user);
        icesiUser.setPassword(encoder.passwordEncoder().encode(user.getPassword()));
        icesiUser.setUserId(UUID.randomUUID());
        if(user.getRoleName().equals("ADMIN") && roleBank()){
            throw eb.exceptionUnauthorized("You don't have permission to create an admin user", "user" );
        }

        icesiUser.setRole(roleRepository.findByName(user.getRoleName()).orElseThrow(() -> {
            throw eb.exceptionNotFound("Role not found", user.getRoleName());
        }));
        return userMapper.toResponse(userRespository.save(icesiUser));
    }

    public Boolean roleBank(){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRespository.findByEmail(username).orElseThrow(()->{
            throw eb.exceptionNotFound("User not found", username);
        });
        return user.getRole().getName().equals("BANK");
    }
}
