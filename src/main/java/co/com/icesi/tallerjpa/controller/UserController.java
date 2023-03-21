package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.controller.api.UserApi;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController(UserApi.BASE_URL)
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    public ResponseUserDTO add(@RequestBody RequestUserDTO user){
        return userService.save(user);
    }

}
