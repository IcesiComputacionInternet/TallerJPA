package icesi.university.accountSystem.controller;

import icesi.university.accountSystem.api.RoleAPI;
import icesi.university.accountSystem.dto.IcesiRoleDTO;
import icesi.university.accountSystem.mapper.IcesiRoleMapper;
import icesi.university.accountSystem.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import static icesi.university.accountSystem.api.RoleAPI.ROLE_BASE_URL;


@RestController
@AllArgsConstructor
public class RoleController implements RoleAPI {

    private RoleService roleService;

    private IcesiRoleMapper icesiRoleMapper;


    @PostMapping(ROLE_BASE_URL+"/add")
    @Override
    public IcesiRoleDTO add(@RequestBody IcesiRoleDTO role) {
        return icesiRoleMapper.fromIcesiRole(roleService.save(role));
    }
    @GetMapping(ROLE_BASE_URL+"/all")
    @Override
    public List<IcesiRoleDTO> getAll() {
        return icesiRoleMapper.fromIcesiRoles(roleService.getAllRoles());
    }
}
