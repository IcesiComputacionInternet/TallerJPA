package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.dto.request.IcesiUserDTO;
import com.icesi.TallerJPA.dto.response.IcesiUserResponseDTO;
import com.icesi.TallerJPA.error.exception.DetailBuilder;
import com.icesi.TallerJPA.error.exception.ErrorCode;
import com.icesi.TallerJPA.error.exception.IcesiException;
import com.icesi.TallerJPA.error.util.IcesiExceptionBuilder;
import com.icesi.TallerJPA.mapper.UserMapper;
import com.icesi.TallerJPA.model.IcesiUser;
import com.icesi.TallerJPA.repository.RoleRepository;
import com.icesi.TallerJPA.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.icesi.TallerJPA.error.util.IcesiExceptionBuilder.createIcesiError;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final UserRespository userRespository;

    private final RoleRepository roleRepository;

    private final IcesiExceptionBuilder eb = new IcesiExceptionBuilder();

    public IcesiUserResponseDTO save(IcesiUserDTO user) {

        Boolean existEmail = userRespository.existsByEmail(user.getEmail());
        Boolean existPhone = userRespository.existsByPhoneNumber(user.getPhoneNumber());

        if(existEmail && existPhone){
            eb.throwExceptionDuplicated("Email and phone are repeated", "user", "email and phone", user.getEmail() + " y " + user.getPhoneNumber());}
        if(existEmail){
            eb.throwExceptionDuplicated("Email is repeated", "user", "email", user.getEmail());}
        if(existPhone){
            eb.throwExceptionDuplicated("Phone is repeated", "user", "phone", user.getPhoneNumber());}

        return createUser(user);
    }

    public IcesiUserResponseDTO createUser(IcesiUserDTO user) {
        IcesiUser icesiUser = userMapper.fromIcesiUser(user);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setIcesiRole(roleRepository.findIcesiRoleByName(
                user.getRolName()).orElseThrow(()-> new IcesiException(
                        "Role not found", createIcesiError("Role not found", HttpStatus.NOT_FOUND, new DetailBuilder(ErrorCode.ERR_404, "User", "Id", icesiUser.getUserId()))
                        )));
        return userMapper.toResponse(userRespository.save(icesiUser));
    }
}
