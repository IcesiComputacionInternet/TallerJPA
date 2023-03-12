package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class IcesiUserController {
    private final IcesiUserService service;
    //TODO: implement necessary endpoints

    @PostMapping
    public IcesiUser create(@RequestBody IcesiUserDTO userDTO){
        return  service.save(userDTO);

    }
}
