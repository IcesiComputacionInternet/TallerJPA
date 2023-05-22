package co.edu.icesi.tallerjpa.api;

import co.edu.icesi.tallerjpa.dto.IcesiUserResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/users")
public interface IcesiUserApi {
    @PostMapping
    IcesiUserResponseDTO getUser(@PathVariable String userEmail);

    List<IcesiUserResponseDTO> getAllUsers();

    @PostMapping("/createUser")
    IcesiUserResponseDTO addUser(@Valid @RequestBody IcesiUserResponseDTO requestUserDTO);
}
