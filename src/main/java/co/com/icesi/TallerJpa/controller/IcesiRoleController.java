package co.com.icesi.TallerJpa.controller;

import co.com.icesi.TallerJpa.controller.api.IcesiRoleApi;
import co.com.icesi.TallerJpa.dto.IcesiRoleDTO;
import co.com.icesi.TallerJpa.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class IcesiRoleController implements IcesiRoleApi {

    private IcesiRoleService icesiRoleService;

    @Override
    public IcesiRoleDTO addIcesiRole(IcesiRoleDTO icesiRoleDTO) {
        return icesiRoleService.saveRole(icesiRoleDTO);
    }

    @Override
    public List<IcesiRoleDTO> addListIcesiRole(List<IcesiRoleDTO> icesiRoleDTOS) {
        return icesiRoleService.saveListRoles(icesiRoleDTOS);
    }

    @Override
    public List<IcesiRoleDTO> getAllIcesiRoles() {
        return icesiRoleService.getAllRoles();
    }

    @Override
    public IcesiRoleDTO getIcesiRole(String name) {
        return icesiRoleService.getRoleByName(name);
    }
}
