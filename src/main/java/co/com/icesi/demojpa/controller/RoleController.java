package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.api.RoleApi;
import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.servicio.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class RoleController implements RoleApi {

    private final RoleService roleService;

    @Override
    @PostMapping
    public RoleCreateDTO createIcesiRole( RoleCreateDTO role){
        return roleService.save(role);
    }


}
