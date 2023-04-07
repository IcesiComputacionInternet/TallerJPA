package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiUserCreateResponseDTO;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository userRepository;
    private final IcesiRoleRepository roleRepository;
    private final IcesiUserMapper userMapper;

    public IcesiUserCreateResponseDTO save(IcesiUserCreateDTO userDTO){
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent() && userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent()){
            throw new RuntimeException("This user email "+userDTO.getEmail()+" and this phone number "+userDTO.getPhoneNumber()+" already exists in the database");
        }

        else if (userRepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw new RuntimeException("This user email "+userDTO.getEmail()+" already exists in the database");
        }

        else if(userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent()){
            throw new RuntimeException("This user phone number "+userDTO.getPhoneNumber()+" already exists in the database");
        }

        IcesiRole role  = roleRepository.findByName(userDTO.getRole().getName()).orElseThrow(() -> new RuntimeException("This role doesn't exist: "+userDTO.getRole()));
        IcesiUser user = userMapper.fromIcesiUserDTO(userDTO);
        user.setUserId(UUID.randomUUID());
        user.setRole(role);
        return userMapper.userToUserDTO(userRepository.save(user));
    }
}

