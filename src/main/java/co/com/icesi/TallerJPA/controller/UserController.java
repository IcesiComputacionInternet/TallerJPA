package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public IcesiUser createIcesiUser(@RequestBody UserCreateDTO user){
        try {
            return userService.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
