package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.dto.response.UserResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiUserAPI.BASE_USER_URL;

@RequestMapping(value = BASE_USER_URL)
public interface IcesiUserAPI {

    String BASE_USER_URL = "/users";

    @PostMapping
    UserResponseDTO createIcesiUser(@Valid @RequestBody UserCreateDTO user);

    @GetMapping("/{userEmail}")
    UserResponseDTO getUserByEmail(@PathVariable String userEmail);

    @GetMapping("/all")
    List<UserResponseDTO> getAllUsers();



}
