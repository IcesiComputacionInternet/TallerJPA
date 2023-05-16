package co.edu.icesi.tallerjpa.api;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiRoleShowDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/icesi_roles")
public interface IcesiRoleApi {
    @PostMapping
    public IcesiRoleShowDTO createRole(@Valid IcesiRoleCreateDTO icesiRoleCreateDTO);

}
