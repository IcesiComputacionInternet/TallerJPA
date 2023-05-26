package co.com.icesi.jpataller.api;

import co.com.icesi.jpataller.dto.IcesiUserCreateDTO;
import co.com.icesi.jpataller.dto.response.IcesiResponseUserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static co.com.icesi.jpataller.api.IcesiUserAPI.BASE_USER_URL;


@RequestMapping(value = BASE_USER_URL)
public interface IcesiUserAPI {

    String BASE_USER_URL = "/users";

    @PostMapping("/")
    IcesiResponseUserDTO createIcesiUser(@Valid @RequestBody IcesiUserCreateDTO user);

}