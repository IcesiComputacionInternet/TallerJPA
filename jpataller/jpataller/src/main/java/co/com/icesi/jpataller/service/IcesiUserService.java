package co.com.icesi.jpataller.service;

import co.com.icesi.jpataller.dto.IcesiUserDTO;
import co.com.icesi.jpataller.mapper.IcesiUserMapper;
import co.com.icesi.jpataller.model.IcesiUser;
import co.com.icesi.jpataller.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;

    private final IcesiUserMapper icesiUserMapper;

    public IcesiUser createUser(IcesiUserDTO user) {
        IcesiUser icesiUser = icesiUserMapper.fromDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        return icesiUserRepository.save(icesiUser);
    }
}
