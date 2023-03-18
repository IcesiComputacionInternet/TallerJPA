package co.com.icesi.TallerJpa.service;

import co.com.icesi.TallerJpa.dto.IcesiUserCreateDTO;
import co.com.icesi.TallerJpa.exceptions.icesiUserExceptions.RoleCantBeNullException;
import co.com.icesi.TallerJpa.exceptions.icesiUserExceptions.UserAttributeAlreadyInUseException;
import co.com.icesi.TallerJpa.mapper.IcesiUserMapper;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;
    private final IcesiUserMapper icesiUserMapper;
    public IcesiUser saveUser(IcesiUserCreateDTO user, IcesiRole role) {
        if(icesiUserRepository.findByEmail(user.getEmail()).isPresent() &&
        icesiUserRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            throw new UserAttributeAlreadyInUseException("Email and phone number are already in use");
        }
        if(icesiUserRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserAttributeAlreadyInUseException("Email is already in use");
        }
        if(icesiUserRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            throw new UserAttributeAlreadyInUseException("Phone number is already in use");
        }
        if(role == null){
            throw new RoleCantBeNullException();
        }
        IcesiUser icesiUser = icesiUserMapper.fromUserDto(user);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setIcesiRole(role);
        return icesiUserRepository.save(icesiUser);
    }
    public IcesiUser getUserById(UUID idString){
        return icesiUserRepository.findById(idString).get();
    }
}
