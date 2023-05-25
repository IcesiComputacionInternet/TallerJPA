package co.com.icesi.TallerJpa.controller.api;

import co.com.icesi.TallerJpa.dto.IcesiUserRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiUserResponseDTO;
import co.com.icesi.TallerJpa.dto.RoleChangeDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@RequestMapping(value = IcesiUserApi.USER_BASE_URL)
public interface IcesiUserApi {

    String USER_BASE_URL = "/api/users";

    @PostMapping
    IcesiUserResponseDTO addIcesiUser(@Valid @RequestBody IcesiUserRequestDTO icesiUserRequestDTO);

    @PatchMapping
    IcesiUserResponseDTO assignRole(@Valid @RequestBody RoleChangeDTO roleChangeDTO);

    @GetMapping("/userId/{userId}")
    IcesiUserResponseDTO getIcesiUserById(@PathVariable String userId);

    @GetMapping("/email/{email}")
    IcesiUserResponseDTO getIcesiUserByEmail(@PathVariable @Valid @Email String email);

    @GetMapping
    List<IcesiUserResponseDTO> getIcesiUsersAll();
}
