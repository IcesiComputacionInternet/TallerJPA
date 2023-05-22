package co.edu.icesi.tallerjpa.runableartefact.api;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(IcesiUserAPI.BASE_URL)
public interface IcesiUserAPI {

    String BASE_URL = "/api/icesi-users";

    @PostMapping("/save")
    String createNewUser(IcesiUserDTO userDTO);

    @PostMapping("/update")
    IcesiUserDTO updateUser(IcesiUserDTO userDTO);

    @GetMapping("/all")
    List<IcesiUser> getAllUsers();
}
