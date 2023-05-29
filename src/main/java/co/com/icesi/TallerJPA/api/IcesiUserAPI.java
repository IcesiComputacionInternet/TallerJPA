package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiUserCreateResponseDTO;
import co.com.icesi.TallerJPA.model.IcesiUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface IcesiUserAPI {
    String BASE_USER_URL = "/users";
    @PostMapping("/create")
    IcesiUserCreateResponseDTO addUser(@Valid @RequestBody IcesiUserCreateDTO userDTO);
    @GetMapping
    List<IcesiUserCreateResponseDTO> getAllUsers();
    @GetMapping("/{userId}")
    IcesiUser getUserById();
}
