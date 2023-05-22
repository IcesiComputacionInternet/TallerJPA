package co.com.icesi.TallerJpa.controller.api;

import co.com.icesi.TallerJpa.dto.IcesiUserRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiUserResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RequestMapping(value = IcesiUserApi.USER_BASE_URL)
public interface IcesiUserApi {

    String USER_BASE_URL = "/api/users";

    @PostMapping
    IcesiUserResponseDTO addIcesiUser(@Valid @RequestBody IcesiUserRequestDTO icesiUserRequestDTO);

    @GetMapping("/userId/{userId}")
    IcesiUserResponseDTO getIcesiUserById(@PathVariable String userId);

    @GetMapping("/email/{email}")
    IcesiUserResponseDTO getIcesiUserByEmail(@PathVariable @Valid @Email String email);
}
