package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.api.IcesiUserAPI;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiUserCreateResponseDTO;
import co.com.icesi.TallerJPA.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiUserAPI.BASE_USER_URL;

@RestController
@RequestMapping(BASE_USER_URL)
@AllArgsConstructor
public class IcesiUserController implements IcesiUserAPI {
    private IcesiUserService userService;

    @Override
    public IcesiUserCreateResponseDTO addUser(IcesiUserCreateDTO userDTO) {
        return userService.save(userDTO);
    }

    @Override
    public List<IcesiUserCreateResponseDTO> getAllUsers() {
        return null;
    }

    @Override
    public IcesiUserCreateResponseDTO getUserById(String userId) {
        return null;
    }
}
