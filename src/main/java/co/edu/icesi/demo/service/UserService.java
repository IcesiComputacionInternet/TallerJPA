package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.UserDTO;
import co.edu.icesi.demo.error.exception.DetailBuilder;
import co.edu.icesi.demo.error.exception.ErrorCode;
import co.edu.icesi.demo.mapper.UserMapper;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.RoleRepository;
import co.edu.icesi.demo.repository.UserRepository;
import co.edu.icesi.demo.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static co.edu.icesi.demo.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDTO save(UserDTO user){
        manageAuthorization(user);
        validateEmailAndPhoneNumber(user);

        IcesiRole icesiRole=roleRepository.findByName(user.getRoleName()).orElseThrow(createIcesiException(
                "User role does not exists",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "User role",user.getRoleName())
        ));
        IcesiUser icesiUser=userMapper.fromIcesiUserDTO(user);
        icesiUser.setRole(icesiRole);
        icesiUser.setPassword(passwordEncoder.encode(user.getPassword()));
        icesiUser.setUserId(UUID.randomUUID());
        return userMapper.fromIcesiUser(userRepository.save(icesiUser));

    }

    public void manageAuthorization(UserDTO user){
        if(IcesiSecurityContext.getCurrentUserRole().equals("BANK") && user.getRoleName().equals("ADMIN")){
            throw createIcesiException(
                    "Bank user cannot create Admin users",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403, "Unauthorized: Bank user cannot create Admin users")
            ).get();
        }
    }

    public void validateEmailAndPhoneNumber(UserDTO user){
        if(userRepository.findByEmail(user.getEmail()).isPresent() && userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){

            throw createIcesiException(
                    "User email and phone number are in use",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User Email and","Phone number" )
            ).get();

        }else if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw createIcesiException(
                    "User email is in use",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User email",user.getEmail() )
            ).get();
        }else if(userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            throw createIcesiException(
                    "User phone number is in use",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Phone Number",user.getPhoneNumber())
            ).get();
        }
    }

    public void AdminAuthorizationOnly(){
        if(!IcesiSecurityContext.getCurrentUserRole().equals("ADMIN")){
            throw createIcesiException(
                    "Unauthorized: Admin only",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403, "Unauthorized: Admin only")
            ).get();
        }
    }

    public UserDTO getUser(String userEmail) {
        AdminAuthorizationOnly();
        return  userMapper.fromIcesiUser(userRepository.findByEmail(userEmail).orElseThrow(
                createIcesiException(
                        "User email not found",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "User email",userEmail )
                )
        ));
    }

    public List<UserDTO> getAllUsers() {
        AdminAuthorizationOnly();
        return userRepository.findAll().stream()
                .map(userMapper::fromIcesiUser)
                .toList();
    }
}
