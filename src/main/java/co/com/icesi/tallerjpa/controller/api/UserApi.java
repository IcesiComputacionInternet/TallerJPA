package co.com.icesi.tallerjpa.controller.api;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping(UserApi.USER_BASE_URL)
public interface UserApi {
    String USER_BASE_URL = "/users";
    @PostMapping
    ResponseUserDTO add(@Valid @RequestBody RequestUserDTO user);
}
