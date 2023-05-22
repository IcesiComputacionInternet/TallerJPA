package co.edu.icesi.demo.api;

import co.edu.icesi.demo.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface UserAPI {

    String BASE_USER_URL="/users";

    @GetMapping("/{userEmail}")
    UserDTO getUser(@PathVariable String userEmail);

    @GetMapping
    List<UserDTO> getAllUsers();

    @PostMapping
    UserDTO addUser(@Valid @RequestBody UserDTO userDTO);


}
