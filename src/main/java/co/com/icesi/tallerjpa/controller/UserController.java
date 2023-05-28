package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.api.UserAPI;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.com.icesi.tallerjpa.api.UserAPI.USER_URL;

@RestController
@RequestMapping(USER_URL)
@AllArgsConstructor
public class UserController implements UserAPI {
    private final UserService userService;
    @Override
    public ResponseUserDTO getUser(String userEmail){
        return userService.getUser(userEmail);
    }

    @GetMapping
    public List<ResponseUserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @Override
    public ResponseUserDTO addUser(@RequestBody RequestUserDTO requestUserDTO){
        return userService.save(requestUserDTO);
    }

    @Override
    public List<ResponseAccountDTO> getAccounts() {
        return userService.getAccounts();
    }
}
