package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.dto.IcesiUserDTO;
import com.icesi.TallerJPA.mapper.UserMapper;
import com.icesi.TallerJPA.model.IcesiUser;
import com.icesi.TallerJPA.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRespository userRespository;
    public IcesiUser save(IcesiUserDTO user) {
        IcesiUser icesiUser = userMapper.fromIcesiUser(user);
        icesiUser.setUserId(UUID.randomUUID());
        return userRespository.save(icesiUser);
    }
}
