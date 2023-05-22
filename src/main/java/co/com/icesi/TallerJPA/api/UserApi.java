package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/user")
public interface UserApi {

    @PostMapping
    IcesiUserDTO createUser(@Valid  @RequestBody IcesiUserDTO userDTO);

    @GetMapping
    List<IcesiUserDTO> getUsers();
}
