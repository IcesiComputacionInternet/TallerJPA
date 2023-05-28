package co.com.icesi.icesiAccountSystem.controller;
import co.com.icesi.icesiAccountSystem.api.UserAPI;
import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseUserDTO;
import co.com.icesi.icesiAccountSystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;


@RestController
@AllArgsConstructor
public class UserController implements UserAPI {

    private final UserService userService;

    @Override
    public ResponseUserDTO getUser(String userEmail) {
        return userService.getUser(userEmail);
    }


    @Override
    public List<ResponseUserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public ResponseUserDTO createUser(RequestUserDTO requestUserDTO) {
        return userService.saveUser(requestUserDTO);
    }

}
