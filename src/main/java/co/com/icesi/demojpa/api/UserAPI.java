package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.ResponseUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static co.com.icesi.demojpa.api.UserAPI.BASE_USER_URL;


@RequestMapping(value = BASE_USER_URL)
public interface UserAPI {

    String BASE_USER_URL = "/users";

    @PostMapping("/")
    ResponseUserDTO createIcesiUser(@Valid @RequestBody UserCreateDTO user);

    @GetMapping ("/currentUser")
    ResponseUserDTO getUser();

}
