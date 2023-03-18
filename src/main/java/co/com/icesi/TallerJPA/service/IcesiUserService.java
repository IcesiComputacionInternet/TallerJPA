package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository iur;
    private final IcesiRoleRepository irr;
    private final IcesiUserMapper im;

    public IcesiUser save(IcesiUserCreateDTO userDTO) {
        validateEmailAndPhoneNumber(userDTO);
        validateRole(userDTO);

        IcesiRole ir = irr.findByRoleName(userDTO.getRole().getName())
                .orElseThrow(() -> new RuntimeException("Not exist this user role in the database"));

        IcesiUser iu = im.fromIcesiUserCreateDTO(userDTO);
        iu.setRole(ir);
        iu.setUserId(UUID.randomUUID());
        iu.setAccounts(new ArrayList<>());
        ir.getUsers().add(iu);

        return iur.save(iu);
    }
    public void validateEmailAndPhoneNumber(IcesiUserCreateDTO userDTO){
        boolean emailExists = iur.findByEmail(userDTO.getEmail()).isPresent();
        boolean phoneExists = iur.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent();

        if(emailExists && phoneExists){
            throw new RuntimeException("User email and phone number is already in use");
        }else if (emailExists){
            throw new RuntimeException("User email is already in use");
        }else if(phoneExists){
            throw new RuntimeException("User phone number is already in use");
        }
    }
    public void validateRole(IcesiUserCreateDTO userDTO){
        Optional.ofNullable(userDTO.getRole())
                .orElseThrow(() -> new RuntimeException("user role is required to continue"));
    }
}

