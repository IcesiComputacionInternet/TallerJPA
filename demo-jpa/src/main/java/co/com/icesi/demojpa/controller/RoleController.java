package co.com.icesi.demojpa.controller;


import co.com.icesi.demojpa.api.RoleAPI;
import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RoleController implements RoleAPI {


    private RoleService roleService;

    @Override
    public RoleCreateDTO addRole(RoleCreateDTO role){
        return roleService.save(role);
    }

    @Override
    public RoleCreateDTO getRoleById(String roleId){
        return null;
    }
}
