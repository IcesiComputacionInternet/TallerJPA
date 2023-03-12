package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/roles")
public class IcesiRoleController {
    private final IcesiRoleService service;
    //TODO: Implement the necessary enpoints

    @PostMapping
    public IcesiRole createRole(@RequestBody IcesiRoleDTO dto){
        return service.save(dto);
    }
}
