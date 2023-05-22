package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.UserAPI;
import co.edu.icesi.demo.dto.UserDTO;
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
    public UserDTO getUser(String userEmail) {

        return userService.getUser(userEmail);
    }

    @Override
    public List<UserDTO> getAllUsers() {

        return userService.getAllUsers();
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {

        return userService.save(userDTO);
    }
}
