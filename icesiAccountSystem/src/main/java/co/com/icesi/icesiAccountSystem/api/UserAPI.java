package co.com.icesi.icesiAccountSystem.api;

import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseUserDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(UserAPI.BASE_USER_URL)
public interface UserAPI {
    String BASE_USER_URL="/users";
    @GetMapping("/{userEmail}")
    ResponseUserDTO getUser(@PathVariable("userEmail")  String userEmail);
    List<ResponseUserDTO> getAllUsers();
    @PostMapping("/create")
    ResponseUserDTO createUser(@Valid @RequestBody RequestUserDTO requestUserDTO);
}
