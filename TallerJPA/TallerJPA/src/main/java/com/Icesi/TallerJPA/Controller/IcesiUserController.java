package com.Icesi.TallerJPA.Controller;

import com.Icesi.TallerJPA.api.UserAPI;
import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.mapper.IcesiUserMapper;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.Icesi.TallerJPA.service.IcesiUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class IcesiUserController implements UserAPI {
    private final IcesiUserService userService;
    private final IcesiUserMapper userMapper;

    @Override
    public IcesiUserDTO createIcesiUser(IcesiUserDTO user) {
        return userService.save(user);
    }

    @Override
    public IcesiUserDTO getUserByEmail(String userEmail) {
        return userMapper.fromIcesiUser(userService.getUserByEmail(userEmail));
    }

    @Override
    public List<IcesiUserDTO> getAllUsers() {
        return userService.getUsers().stream().map(userMapper::fromIcesiUser).collect(Collectors.toList());
    }
}
