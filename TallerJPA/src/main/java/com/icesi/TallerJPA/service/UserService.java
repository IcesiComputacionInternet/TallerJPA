package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.dto.request.IcesiUserDTO;
import com.icesi.TallerJPA.dto.response.IcesiUserResponseDTO;
import com.icesi.TallerJPA.mapper.UserMapper;
import com.icesi.TallerJPA.model.IcesiUser;
import com.icesi.TallerJPA.repository.RoleRepository;
import com.icesi.TallerJPA.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final UserRespository userRespository;

    private final RoleRepository roleRepository;

    public IcesiUserResponseDTO save(IcesiUserDTO user) {

        Boolean existEmail = userRespository.existsByEmail(user.getEmail());
        Boolean existPhone = userRespository.existsByPhoneNumber(user.getPhoneNumber());

        if(existEmail && existPhone){throw new RuntimeException("Email and Phone is repeated");}
        if(existEmail){throw new RuntimeException("Email is repeated");}
        if(existPhone){throw new RuntimeException("Phone is repeated");}

        return createUser(user);
    }

    public IcesiUserResponseDTO createUser(IcesiUserDTO user){
        IcesiUser icesiUser = userMapper.fromIcesiUser(user);
        icesiUser.setUserId(UUID.randomUUID());
        //icesiUser.setIcesiRole(roleRepository.findIcesiRoleByName(
        //       user.getRolName()).orElseThrow(()-> new RuntimeException("Role not found")));
        return userMapper.toResponse(userRespository.save(icesiUser));
    }
}
