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
    private final IcesiUserRepository mainRepository;
    private final IcesiRoleRepository roleRepository;
    private final IcesiUserMapper mapper;

    public boolean save(IcesiUserDTO dto){

        if(mainRepository.findByEmail(dto.getEmail()).isPresent() && mainRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()){
           throw new RuntimeException("This email and phone  already exist");
        }else if(mainRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("Email already exist");
        }else if(mainRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()){
            throw new RuntimeException("Phone already exist");
        }else{
            IcesiUser user = mapper.fromUserDto(dto);
            user.setUserID(UUID.randomUUID());
            user.setAccounts(new ArrayList<>());

            if (user.getRole()!=null && roleRepository.findByName(user.getRole().getName()).isPresent()) {
                IcesiRole relation  = roleRepository.findByName(user.getRole().getName()).get();
                relation.getUsers().add(user);
                user.setRole(relation);
                mainRepository.save(user);
                roleRepository.save(relation);
                return true;
            } else {
                throw new RuntimeException("This role doesn't exist or is null");
            }

        }
    }

    public List<IcesiUserDTO> getUsers(){
        return mainRepository.findAll().stream().map(mapper::fromIcesiUser).collect(Collectors.toList());
    }


}
