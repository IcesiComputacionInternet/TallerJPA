package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAuthorities;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.runableartefact.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthoritiesService {

    private final IcesiUserRepository icesiUserRepository;

    public void validateAuthorities(String authorityName){
        Optional<IcesiUser> icesiUser = icesiUserRepository.findById(UUID.fromString(IcesiSecurityContext.getCurrentUserId()));
        icesiUser.ifPresent(user -> {
            if(user.getAuthorities().stream().map(IcesiAuthorities::getAuthority).noneMatch(authority -> authority.equals(authorityName))) {
                throw new RuntimeException("You are not authorized to perform this action");
            }
        });
    }
}
