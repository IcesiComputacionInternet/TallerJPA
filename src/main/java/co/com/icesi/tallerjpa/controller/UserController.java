package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.controller.api.UserApi;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseUserDTO add(RequestUserDTO user){
        return userService.save(user);
    }

}
