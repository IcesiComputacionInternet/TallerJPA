package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class IcesiUserController {
    private final IcesiUserService service;


    @PostMapping
    public String create(@RequestBody IcesiUserDTO userDTO){
        return  service.save(userDTO) ? "User created succesfully" : "";

    }

    @GetMapping
    public List<IcesiUserDTO> getIcesiUser(){
        return service.getUsers();
    }
}
