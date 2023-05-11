package co.edu.icesi.tallerjpa.api;

import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserShowDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/icesi_users")
public interface IcesiUserApi {
    @PostMapping
    public IcesiUserShowDTO createIcesiUser(@RequestBody IcesiUserCreateDTO userDTO);
}
