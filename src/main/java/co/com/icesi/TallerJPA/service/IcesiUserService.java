package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.error.exception.ApplicationError;
import co.com.icesi.TallerJPA.error.exception.ApplicationException;
import co.com.icesi.TallerJPA.error.exception.ErrorCode;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import co.com.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static co.com.icesi.TallerJPA.error.util.ErrorManager.createDetail;
import static co.com.icesi.TallerJPA.error.util.ErrorManager.sendDetails;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository userRepository;
    private final IcesiRoleRepository roleRepository;
    private final IcesiUserMapper mapper;
    private final PasswordEncoder encoder;
    private final IcesiSecurityContext securityContext;

    public IcesiUserDTO save(IcesiUserDTO dto){
        checkUserToCreate(dto);
        boolean haveBothRestrictions = userRepository.findByEmail(dto.getEmail()).isPresent() && userRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent();
        if(haveBothRestrictions){
            throw new RuntimeException("This email and phone already exist: "+dto.getEmail()+" and "+dto.getPhoneNumber());
        }
        userRepository.findByEmail((dto.getEmail())).ifPresent(e ->{
            throw new RuntimeException("This email already exist: "+e.getEmail());
        });
        userRepository.findByPhoneNumber((dto.getPhoneNumber())).ifPresent(e ->{
            throw new RuntimeException("This phone already exist: "+e.getPhoneNumber());
        });


        IcesiUser user = mapper.fromUserDto(dto);
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setUserID(UUID.randomUUID());
        user.setAccounts(new ArrayList<>());

        IcesiRole roleRelation  = roleRepository.findByName(user.getRole().getName()).orElseThrow(() -> new RuntimeException("This role doesn't exist") );

        roleRelation.getUsers().add(user);
        user.setRole(roleRelation);
        userRepository.save(user);
        roleRepository.save(roleRelation);
        return mapper.fromIcesiUser(user);

    }

    private void checkUserToCreate(IcesiUserDTO dto){
        var currentRole = securityContext.getCurrenUserRole();
        if(dto.getRole().getName().equalsIgnoreCase("ADMIN") && currentRole.equalsIgnoreCase("BANK")){
            throw createApplicationException().get();
        }

    }

    public List<IcesiUserDTO> getUsers(){
        return userRepository.findAll().stream().map(mapper::fromIcesiUser).collect(Collectors.toList());
    }

    private static Supplier<ApplicationException> createApplicationException() {
        return () -> new ApplicationException("Cant create the user", ApplicationError.builder()
                .details(List.of(sendDetails(createDetail("You cant create an user with ADMIN ROLE", ErrorCode.ERROR_USER_TO_CREATE))))
                .status(HttpStatus.FORBIDDEN)
                .build());
    }



}
