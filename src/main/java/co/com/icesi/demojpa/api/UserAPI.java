package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.ResponseUserDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static co.com.icesi.demojpa.api.UserAPI.BASE_USER_URL;


@RequestMapping(value = BASE_USER_URL)
public interface UserAPI {

    String BASE_USER_URL = "/users";

    @PostMapping("/")
    ResponseUserDTO createIcesiUser(@Valid @RequestBody UserCreateDTO user);

    @CrossOrigin
    @GetMapping ("/currentUser")
    ResponseUserDTO getUser();

}
