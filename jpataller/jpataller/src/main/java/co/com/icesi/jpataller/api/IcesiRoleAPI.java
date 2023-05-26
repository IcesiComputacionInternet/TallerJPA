package co.com.icesi.jpataller.api;

import co.com.icesi.jpataller.dto.RoleCreateDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static co.com.icesi.jpataller.api.IcesiRoleAPI.BASE_ROLE_URL;

@RequestMapping(value = BASE_ROLE_URL)
public interface IcesiRoleAPI {
    String BASE_ROLE_URL = "/roles";

    @PostMapping("/")
    RoleCreateDTO createIcesiRole(@Valid @RequestBody RoleCreateDTO role);

}