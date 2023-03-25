package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository userRepository;
    private final IcesiRoleRepository roleRepository;
    private final IcesiUserMapper mapper;

    public IcesiUserDTO save(IcesiUserDTO dto){
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
        user.setUserID(UUID.randomUUID());
        user.setAccounts(new ArrayList<>());

        IcesiRole roleRelation  = roleRepository.findByName(user.getRole().getName()).orElseThrow(() -> new RuntimeException("This role doesn't exist") );


        roleRelation.getUsers().add(user);
        user.setRole(roleRelation);
        userRepository.save(user);
        roleRepository.save(roleRelation);
        return mapper.fromIcesiUser(user);

    }

    public List<IcesiUserDTO> getUsers(){
        return userRepository.findAll().stream().map(mapper::fromIcesiUser).collect(Collectors.toList());
    }


}
