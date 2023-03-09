package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public void createIcesiUser(@RequestBody UserCreateDTO user) {
        userService.save(user);
    }


}
