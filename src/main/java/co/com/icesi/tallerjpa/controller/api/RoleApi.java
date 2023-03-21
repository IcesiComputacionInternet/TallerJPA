package co.com.icesi.tallerjpa.controller.api;

import co.com.icesi.tallerjpa.dto.RoleDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface RoleApi {
    String BASE_URL = "/roles";
    @PostMapping("/add")
    RoleDTO add(@RequestBody RoleDTO role);
}
