package co.edu.icesi.tallerjpa.runableartefact.service.security;

import co.edu.icesi.tallerjpa.runableartefact.error.enums.ErrorCode;
import co.edu.icesi.tallerjpa.runableartefact.error.exception.CustomException;
import co.edu.icesi.tallerjpa.runableartefact.error.util.CustomDetail;
import co.edu.icesi.tallerjpa.runableartefact.error.util.CustomError;
import co.edu.icesi.tallerjpa.runableartefact.model.SecurityUser;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@AllArgsConstructor
public class UserManagementService implements UserDetailsService {

    private final IcesiUserRepository icesiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return icesiUserRepository.findByEmail(username).map(SecurityUser::new)
                .orElseThrow(() -> new CustomException(CustomError.builder()
                        .details(Collections.singletonList(
                                CustomDetail.builder().errorCode(ErrorCode.ERR_404.getCode()).errorMessage(ErrorCode.ERR_404.getMessage()).build()))
                        .status(HttpStatus.FORBIDDEN)
                        .build()));
    }
}
