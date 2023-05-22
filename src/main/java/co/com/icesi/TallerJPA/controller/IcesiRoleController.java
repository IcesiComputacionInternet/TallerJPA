package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.api.IcesiRoleAPI;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiRoleAPI.BASE_ROLE_URL;

@RestController
@RequestMapping(BASE_ROLE_URL)
@AllArgsConstructor
public class IcesiRoleController implements IcesiRoleAPI {
    private IcesiRoleService roleService;

    @Override
    public IcesiRoleCreateDTO addRole(IcesiRoleCreateDTO roleDTO) {
        return roleService.save(roleDTO);
    }

    @Override
    public List<IcesiRoleCreateDTO> getAllRoles() {
        return null;
    }

    @Override
    public IcesiRoleCreateDTO getRoleByName(String rolaName) {
        return null;
    }
}
