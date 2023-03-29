package co.com.icesi.tallerjpa.controller.api;

import co.com.icesi.tallerjpa.dto.RoleDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = RoleApi.ROLE_BASE_URL)
public interface RoleApi {
    String ROLE_BASE_URL = "/roles";
    @PostMapping
    RoleDTO add(@RequestBody RoleDTO role);
}
