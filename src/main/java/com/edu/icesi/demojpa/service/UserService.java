package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.dto.UserCreateDTO;
import com.edu.icesi.demojpa.mapper.UserMapper;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.model.IcesiUser;
import com.edu.icesi.demojpa.repository.RoleRepository;
import com.edu.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public IcesiUser save(UserCreateDTO user){
        verifyFields(user);
        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(idGenerator());
        icesiUser.setRole(findRole(user.getRoleType()));
        return userRepository.save(icesiUser);
    }

    public void verifyFields(UserCreateDTO user){
        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        String role = user.getRoleType();
        boolean emailIsUsed = isEmailInUse().test(email);
        boolean phoneNumberIsUsed = isPhoneNumberInUse().test(phoneNumber);
        boolean roleDontExists = isInvalidRole().test(role);

        if(emailIsUsed && phoneNumberIsUsed){
            fieldsAreRepeatedException("email", "phone-number");
        } else if(emailIsUsed) {
            fieldIsRepeatedException("email");
        } else if(phoneNumberIsUsed){
            fieldIsRepeatedException("phone-number");
        } else if(roleDontExists) {
            invalidRoleException(role);
        }
    }

    public IcesiRole findRole(String roleName){
        Optional<IcesiRole> icesiRole = roleRepository.findRoleByName(roleName);
        return icesiRole.orElseThrow(() -> new RuntimeException("The role doesn't exist"));
    }

    public UUID idGenerator(){
        return UUID.randomUUID();
    }

    public Predicate<String> isEmailInUse(){
        return (email) -> userRepository.findUserByEmail(email).isPresent();
    }

    public Predicate<String> isPhoneNumberInUse(){
        return (phoneNumber) -> userRepository.finUserByPhoneNumber(phoneNumber).isPresent();
    }

    public Predicate<String> isInvalidRole(){
        return (role) -> roleRepository.findRoleByName(role).isEmpty();
    }

    private void fieldsAreRepeatedException(String firstField, String secondField) {
        throw new RuntimeException("The "+firstField+" and "+secondField+" are already in use");
    }

    private void fieldIsRepeatedException(String field) {
        throw new RuntimeException("The "+field+" is already in use");
    }

    private void invalidRoleException(String role){
        throw new RuntimeException("The role with name "+role+" doesn't exist");
    }
}
