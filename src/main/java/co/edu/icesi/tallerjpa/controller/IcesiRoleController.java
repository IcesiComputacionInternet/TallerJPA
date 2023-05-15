package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.api.IcesiRoleApi;
import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiRoleShowDTO;
import co.edu.icesi.tallerjpa.service.IcesiRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class IcesiRoleController implements IcesiRoleApi {
    IcesiRoleService icesiRoleService;
    @Override
    public IcesiRoleShowDTO createRole(@Valid IcesiRoleCreateDTO icesiRoleCreateDTO) {
        return icesiRoleService.save(icesiRoleCreateDTO);
    }
}
