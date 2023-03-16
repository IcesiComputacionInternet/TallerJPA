package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserShowDTO;
import co.edu.icesi.tallerjpa.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@AllArgsConstructor
public class IcesiUserController {
    private final IcesiUserService icesiUserService;

    @PostMapping("/users")
    public IcesiUserShowDTO createIcesiUser(@RequestBody IcesiUserCreateDTO userDTO){
        return icesiUserService.save(userDTO);
    }
}
