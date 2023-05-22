package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.api.UserAPI;
import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.UserResponseDTO;
import co.com.icesi.demojpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController implements UserAPI {

    private final UserService userService;

    @Override
    public UserResponseDTO createIcesiUser(UserCreateDTO user) {
        return userService.save(user);
    }
}
