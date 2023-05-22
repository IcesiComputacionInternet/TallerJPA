package icesi.university.accountSystem.controller;

import icesi.university.accountSystem.api.UserAPI;
import icesi.university.accountSystem.dto.RequestUserDTO;
import icesi.university.accountSystem.dto.ResponseUserDTO;
import icesi.university.accountSystem.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@AllArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;
    @GetMapping(BASE_USER_URL+"/{userEmail}")
    @Override
    public ResponseUserDTO getUser(@PathVariable String userEmail) {
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
    @PutMapping(BASE_USER_URL+"/assignRole/{userEmail}/{roleName}")
    @Override
    public ResponseUserDTO assignRole(@PathVariable String userEmail, @PathVariable String roleName) {
        return userService.assignRole(userEmail,roleName);
    }
}
