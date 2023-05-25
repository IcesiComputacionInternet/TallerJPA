package co.com.icesi.TallerJpa.controller;

import co.com.icesi.TallerJpa.controller.api.IcesiUserApi;
import co.com.icesi.TallerJpa.dto.IcesiUserRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiUserResponseDTO;
import co.com.icesi.TallerJpa.dto.RoleChangeDTO;
import co.com.icesi.TallerJpa.security.IcesiSecurityContext;
import co.com.icesi.TallerJpa.service.IcesiUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class IcesiUserController implements IcesiUserApi {

    private IcesiUserService icesiUserService;

    @Override
    public IcesiUserResponseDTO addIcesiUser(IcesiUserRequestDTO icesiUserRequestDTO) {
        String actualUserRole = IcesiSecurityContext.getCurrentUserRole();
        return icesiUserService.saveUser(icesiUserRequestDTO, actualUserRole);
    }

    @Override
    public IcesiUserResponseDTO assignRole(RoleChangeDTO roleChangeDTO) {
        return icesiUserService.assignRole(roleChangeDTO);
    }

    @Override
    public IcesiUserResponseDTO getIcesiUserById(String userId) {
        return icesiUserService.getUserById(UUID.fromString(userId));
    }

    @Override
    public IcesiUserResponseDTO getIcesiUserByEmail(String email) {
        return icesiUserService.getUserByEmail(email);
    }

    @Override
    public List<IcesiUserResponseDTO> getIcesiUsersAll() {
        return icesiUserService.getAllUsers();
    }
}
