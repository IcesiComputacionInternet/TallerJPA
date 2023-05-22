package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.ActionResultDTO;
import co.com.icesi.TallerJPA.dto.AssingRoleDTO;
import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/role")
public interface RoleApi {

    @PostMapping
    IcesiRoleDTO createRole(@Valid @RequestBody IcesiRoleDTO roleDTO);

    @GetMapping
    List<IcesiRoleDTO> getRoles();
    
    @PatchMapping
    ActionResultDTO assingRoleToUser(@Valid @RequestBody AssingRoleDTO assigRoleDTO);
}
