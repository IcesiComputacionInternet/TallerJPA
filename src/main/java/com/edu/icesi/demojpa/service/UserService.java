package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.dto.UserCreateDTO;
import com.edu.icesi.demojpa.mapper.UserMapper;
import com.edu.icesi.demojpa.model.IcesiUser;
import com.edu.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public IcesiUser save(UserCreateDTO user){
        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        boolean emailIsUsed = isEmailInUse(email);
        boolean phoneNumberIsUsed = isPhoneNumberInUse(phoneNumber);

        if(emailIsUsed && phoneNumberIsUsed){
            fieldsAreRepeatedException("email", "phone-number");
        } else if(emailIsUsed) {
            fieldIsRepeatedException("email");
        } else if(phoneNumberIsUsed){
            fieldIsRepeatedException("phone-number");
        }

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(userIdGenerator());

        //Falta verificar que el role no sea nulo

        return userRepository.save(icesiUser);
    }

    public UUID userIdGenerator(){
        return UUID.randomUUID();
    }

    private boolean isEmailInUse(String email){
        return userRepository.findUserByEmail(email).isPresent();
    }

    private boolean isPhoneNumberInUse(String phoneNumber){
        return userRepository.finUserByPhoneNumber(phoneNumber).isPresent();
    }

    private void fieldsAreRepeatedException(String firstField, String secondField) {
        throw new RuntimeException("The "+firstField+" and "+secondField+" are already in use");
    }

    private void fieldIsRepeatedException(String field) {
        throw new RuntimeException("The "+field+" is already in use");
    }
}
