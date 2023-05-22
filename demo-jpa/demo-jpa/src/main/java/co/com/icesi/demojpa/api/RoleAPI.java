package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.request.RoleCreateDTO;
import co.com.icesi.demojpa.dto.response.RoleResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping(RoleAPI.ROLE_BASE_URL)
public interface RoleAPI {
    String ROLE_BASE_URL = "/role";
    @PostMapping
    RoleResponseDTO createIcesiRole(@RequestBody @Valid RoleCreateDTO role);
}
