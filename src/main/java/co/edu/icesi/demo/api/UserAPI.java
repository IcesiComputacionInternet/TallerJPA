package co.edu.icesi.demo.api;

import co.edu.icesi.demo.dto.UserCreateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface UserAPI {

    String BASE_USER_URL="/users";

    @GetMapping("/{userEmail}")
    UserCreateDTO getUser(@PathVariable String userEmail);

    @GetMapping
    List<UserCreateDTO> getAllUsers();

    @PostMapping
    UserCreateDTO addUser(@Valid @RequestBody UserCreateDTO userCreateDTO);


}
