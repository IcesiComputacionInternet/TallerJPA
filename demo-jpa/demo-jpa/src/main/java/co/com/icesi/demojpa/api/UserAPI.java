package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.UserResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;

@RequestMapping(UserAPI.USER_BASE_URL)
public interface UserAPI {
    String USER_BASE_URL = "/user";
    @PostMapping
    UserResponseDTO createIcesiUser(@Valid @RequestBody UserCreateDTO user);
}
