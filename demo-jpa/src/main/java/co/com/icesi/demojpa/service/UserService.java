package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.error.exception.DetailBuilder;
import co.com.icesi.demojpa.error.exception.ErrorCode;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static co.com.icesi.demojpa.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public UserCreateDTO save(UserCreateDTO user){

        validatePhoneAndPhone(user.getPhoneNumber(), user.getEmail());
        validateRole(user.getRole());
        validateAuthentication(userMapper.fromIcesiUserDTO(user));

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());

        return userMapper.fromIcesiUser(userRepository.save(icesiUser));
    }

    public boolean validateEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean validatePhone(String phone){
        return userRepository.findByPhone(phone).isPresent();
    }

    public void validatePhoneAndPhone(String phoneNumber, String mail){
        boolean email = validateEmail(mail);
        boolean phone = validatePhone(phoneNumber);

        if(email && phone){
            throw createIcesiException(
                    "User with both e-mail and phone already exists",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User", "E-mail", mail),
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User", "Phone", phoneNumber)
            ).get();}
        if (email){
            throw createIcesiException(
                    "User with this e-mail already exists",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User", "E-mail", mail)
            ).get();}
        if (phone){
            throw createIcesiException(
                    "User with this phone already exists",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User", "Phone", phoneNumber)
            ).get();}
    }

    public void validateRole(RoleCreateDTO roleName){

        Optional.ofNullable(roleName).orElseThrow(createIcesiException(
                "User must have a role",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_REQUIRED_FIELD, "Role", "Name")
        ));


        roleRepository.findByName(roleName.getName()).orElseThrow(createIcesiException(
                "Role does not exist",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_404, "Role", "Name", roleName.getName())
        ));
    }

    public void validateAuthentication(IcesiUser user){
        if (IcesiSecurityContext.getCurrentUserRole().equals("BANK") && user.getRole().getName().equals("ADMIN")) {
            throw createIcesiException(
                    "Unauthorized: You can't create an admin user",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403, "Unauthorized: Admin only")
            ).get();
        }
    }


}
