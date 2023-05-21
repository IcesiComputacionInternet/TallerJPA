package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static co.com.icesi.demojpa.api.RoleApi.BASE_ROLE_URL;

@RequestMapping(value = BASE_ROLE_URL)
public interface RoleApi {
    String BASE_ROLE_URL = "/roles";

    @PostMapping("/")
    RoleCreateDTO createIcesiRole(@Valid @RequestBody RoleCreateDTO role);

}
