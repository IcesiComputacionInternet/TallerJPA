package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.UserAPI;
import co.edu.icesi.demo.dto.UserCreateDTO;
import co.edu.icesi.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.edu.icesi.demo.api.UserAPI.BASE_USER_URL;

@RestController
@RequestMapping(BASE_USER_URL)
@AllArgsConstructor
public class UserController implements UserAPI {

    private final UserService userService;
    @Override
    public UserCreateDTO getUser(String userEmail) {

        return null;
    }

    @Override
    public List<UserCreateDTO> getAllUsers() {

        return null;
    }

    @Override
    public UserCreateDTO addUser(UserCreateDTO userCreateDTO) {

        return userService.save(userCreateDTO);
    }
}
