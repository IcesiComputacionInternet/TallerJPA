package co.com.icesi.tallerjpa.controller.api;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserApi {

    String BASE_URL = "/users";
    @PostMapping("/add")
    ResponseUserDTO add(@RequestBody RequestUserDTO user);
}
