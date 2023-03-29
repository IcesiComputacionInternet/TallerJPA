package co.edu.icesi.tallerjpa.runableartefact.api;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(IcesiUserAPI.BASE_URL)
public interface IcesiUserAPI {

    String BASE_URL = "/api/icesi-users";

    @PostMapping("/save")
    String createNewUser(IcesiUserDTO userDTO);


}
