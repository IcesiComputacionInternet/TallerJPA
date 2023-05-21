package icesi.university.accountSystem.controller;

import icesi.university.accountSystem.api.UserAPI;
import icesi.university.accountSystem.dto.RequestUserDTO;
import icesi.university.accountSystem.dto.ResponseUserDTO;
import icesi.university.accountSystem.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import static icesi.university.accountSystem.api.UserAPI.BASE_USER_URL;
@RestController
@AllArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;
    @GetMapping(BASE_USER_URL+"/{userEmail}")
    @Override
    public ResponseUserDTO getUser(String userEmail) {
        return userService.getUser(userEmail);
    }

    @GetMapping(BASE_USER_URL+"/all")
    @Override
    public List<ResponseUserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(BASE_USER_URL+"/add")
    @Override
    public ResponseUserDTO addUser(@RequestBody RequestUserDTO requestUserDTO) {
        return userService.save(requestUserDTO);
    }
}
