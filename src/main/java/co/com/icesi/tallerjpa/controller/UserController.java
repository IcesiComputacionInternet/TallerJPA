package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.controller.api.UserApi;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseUserDTO add(@Valid @RequestBody RequestUserDTO user){
        return userService.save(user);
    }

}
