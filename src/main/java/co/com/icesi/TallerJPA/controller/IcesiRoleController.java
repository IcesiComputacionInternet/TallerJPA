package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/roles")
public class IcesiRoleController {
    private final IcesiRoleService service;


    @PostMapping
    public IcesiRole createRole(@RequestBody IcesiRoleDTO dto){
        return service.save(dto);
    }

    @GetMapping
    public List<IcesiRoleDTO> getRoles(){
        return service.getRoles();
    }

}
