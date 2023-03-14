package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.CreateIcesiUserDTO;
import co.edu.icesi.tallerjpa.dto.ShowIcesiUserDTO;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;
    private final IcesiUserMapper icesiUserMapper;

    public ShowIcesiUserDTO save(CreateIcesiUserDTO createIcesiUserDTO){
        String messageError = "";
        if(!isEmailUnique(createIcesiUserDTO.getEmail())){
            messageError += "There is already a user with the email " + createIcesiUserDTO.getEmail() + "\n";
        }
        if(!isPhoneNumberUnique(createIcesiUserDTO.getPhoneNumber())){
            messageError += "There is already a user with the phone number " + createIcesiUserDTO.getPhoneNumber() + "\n";
        }
        if(!messageError.equals("")){
            throw new RuntimeException(messageError);
        }
        IcesiUser icesiUser = icesiUserMapper.fromCreateIcesiUserDTO(createIcesiUserDTO);
        icesiUser.setUserId(UUID.randomUUID());
        return icesiUserMapper.fromIcesiUserToShow(icesiUserRepository.save(icesiUser));
    }
    private boolean isEmailUnique(String email){
        if(icesiUserRepository.findByEmail(email).isPresent()){
            return false;
        }
        return true;
    }

    private boolean isPhoneNumberUnique(String phoneNumber){
        if(icesiUserRepository.findByPhoneNumber(phoneNumber).isPresent()){
            return false;
        }
        return true;
    }
}
