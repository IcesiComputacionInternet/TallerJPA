package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.api.UserApi;
import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class IcesiUserController implements UserApi {
    private final IcesiUserService userService;

    @Override
    public IcesiUserDTO createUser(IcesiUserDTO userDTO) {
        return userService.save(userDTO);
    }

    @Override
    public List<IcesiUserDTO> getUsers() {
        return userService.getUsers();
    }
}
