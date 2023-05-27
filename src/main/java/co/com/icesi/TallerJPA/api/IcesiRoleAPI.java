package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiRoleCreateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface IcesiRoleAPI {
    String BASE_ROLE_URL = "/roles";
    @PostMapping
    IcesiRoleCreateDTO addRole(@Valid @RequestBody IcesiRoleCreateDTO roleDTO);
    @GetMapping
    List<IcesiRoleCreateDTO> getAllRoles();
    @GetMapping("/{name}")
    IcesiRoleCreateDTO getRoleByName(@PathVariable String rolaName);
}
