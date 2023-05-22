package co.com.icesi.tallerjpa.controller;

import co.com.icesi.tallerjpa.controller.api.UserApi;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.security.SecurityContext;
import co.com.icesi.tallerjpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseUserDTO add(RequestUserDTO user){
        var role = SecurityContext.getCurrentUserRole();
        return userService.save(user, role);
    }


}
