package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.api.RoleApi;
import co.com.icesi.TallerJPA.dto.ActionResultDTO;
import co.com.icesi.TallerJPA.dto.AssingRoleDTO;
import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class IcesiRoleController  implements RoleApi {
    private final IcesiRoleService roleService;

    @Override
    public IcesiRoleDTO createRole(IcesiRoleDTO roleDTO) {
        return roleService.save(roleDTO);
    }

    @Override
    public List<IcesiRoleDTO> getRoles() {
        return roleService.getRoles();
    }

    @Override
    public ActionResultDTO assingRoleToUser(AssingRoleDTO assigRoleDTO) {
        return roleService.assingRole(assigRoleDTO);
    }
}
