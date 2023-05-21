package co.edu.icesi.tallerjpa.api;

import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserShowDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/icesi_users")
public interface IcesiUserApi {
    public static final String ROOT_PATH = "/icesi_users";
    @PostMapping
    public IcesiUserShowDTO createIcesiUser(@Valid @RequestBody IcesiUserCreateDTO userDTO);

    @PatchMapping("{icesiUserId}/role/{roleName}")
    public IcesiUserShowDTO assignRole(@PathVariable("icesiUserId") String icesiUserId, @PathVariable("roleName") String roleName);
}
