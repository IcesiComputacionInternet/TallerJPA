package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.api.IcesiUserAPI;
import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.dto.response.UserResponseDTO;
import co.com.icesi.TallerJPA.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiUserAPI.BASE_USER_URL;

@RestController
@AllArgsConstructor
public class UserController implements IcesiUserAPI {
    private final UserService userService;

    @Override
    public UserResponseDTO getUserByEmail(String userEmail) {
        return userService.getUserByEmail(userEmail);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public UserResponseDTO createIcesiUser(@RequestBody @Valid UserCreateDTO user) {
        return userService.save(user);
    }


}
