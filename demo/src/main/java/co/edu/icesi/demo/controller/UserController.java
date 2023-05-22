package co.edu.icesi.demo.controller;

import co.edu.icesi.demo.api.UserApi;
import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController implements UserApi {
    private final IcesiUserService icesiUserService;

    @Override
    public IcesiUserDto createUser(IcesiUserDto user) {
        return icesiUserService.saveUser(user);
    }
}
