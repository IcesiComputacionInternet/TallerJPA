package co.edu.icesi.tallerjpa.api;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiRoleShowDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/icesi_roles")
public interface IcesiRoleApi {
    public static final String ROOT_PATH = "/icesi_roles";
    @PostMapping("admin")
    public IcesiRoleShowDTO createRole(@Valid @RequestBody IcesiRoleCreateDTO icesiRoleCreateDTO);

}