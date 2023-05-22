package co.edu.icesi.demo.api;

import co.edu.icesi.demo.dto.IcesiUserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/Users")
public interface UserApi {

    @PostMapping("/add/user")
    public IcesiUserDto createUser(@RequestBody @Valid IcesiUserDto user);
}
