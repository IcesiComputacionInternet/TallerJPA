package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository mainRepository;
    private final IcesiRoleRepository roleRepository;
    private final IcesiUserMapper mainMapper;


    //TODO: implement all the necessary methods

    public IcesiUser save(IcesiUserDTO dto){

        if(mainRepository.findByEmail(dto.getEmail()).isPresent() && mainRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()){
           throw new RuntimeException("This email and phone  already exist");
        }else if(mainRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("Email already exist");
        }else if(mainRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()){
            throw new RuntimeException("Phone already exist");
        }else{
            IcesiUser user = mainMapper.fromUserDto(dto);
            user.setUserID(UUID.randomUUID());

            Optional<IcesiRole> role = Optional.of(user.getRole());
            if (roleRepository.findByName(role.get().getName()).isPresent()) {
                return mainRepository.save(user);
            } else {
                throw new RuntimeException("This role doesn't exist");
            }

        }
    }


}
