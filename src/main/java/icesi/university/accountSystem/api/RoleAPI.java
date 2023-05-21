package icesi.university.accountSystem.api;

import icesi.university.accountSystem.dto.IcesiRoleDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface RoleAPI {
    String ROLE_BASE_URL = "/roles";

    IcesiRoleDTO add(@Valid @RequestBody IcesiRoleDTO role);


    Iterable<IcesiRoleDTO> getAll();
}
