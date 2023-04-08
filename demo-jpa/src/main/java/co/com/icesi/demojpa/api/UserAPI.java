package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.ResponseUserDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface UserAPI {

    String BASE_USER_URL = "/users";

    @GetMapping("{userEmail}")
    ResponseUserDTO getUser(@PathVariable String userEmail);

    List<ResponseUserDTO> getAllUsers();

    @PostMapping
    ResponseUserDTO addUser(UserCreateDTO requestUserDTO);

}
