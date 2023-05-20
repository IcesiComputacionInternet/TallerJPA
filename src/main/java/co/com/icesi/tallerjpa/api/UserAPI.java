package co.com.icesi.tallerjpa.api;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface UserAPI {
    String USER_URL = "/users";

    @GetMapping("/{userEmail}")
    ResponseUserDTO getUser(@PathVariable String userEmail);

    List<ResponseUserDTO> getAllUsers();

    @PostMapping("/createUser")
    ResponseUserDTO addUser(@Valid @RequestBody RequestUserDTO requestUserDTO);
}
