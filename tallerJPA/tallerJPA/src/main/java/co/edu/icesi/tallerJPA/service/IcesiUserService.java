package co.edu.icesi.tallerJPA.service;

import co.edu.icesi.tallerJPA.dto.IcesiUserDTO;
import co.edu.icesi.tallerJPA.exception.ExistingException;
import co.edu.icesi.tallerJPA.mapper.IcesiUserMapper;
import co.edu.icesi.tallerJPA.model.IcesiUser;
import co.edu.icesi.tallerJPA.repository.RoleRepository;
import co.edu.icesi.tallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;
    private final RoleRepository roleRepository;
    private final IcesiUserMapper icesiUserMapper;
@SneakyThrows
    public IcesiUser save(IcesiUserDTO icesiUserDTO){
    boolean checkEmail = icesiUserRepository.isByEmail(icesiUserDTO.getEmail());
    boolean checkPhone = icesiUserRepository.findByPhone(icesiUserDTO.getPhoneNumber());

    if(checkEmail && checkPhone)
        throw new ExistingException("Email and phone already exists");
    if (checkEmail)
        throw new ExistingException("Email  is in use");
    if (checkPhone)
        throw new ExistingException("Phone is in use");


    icesiUserDTO.setUserId(UUID.randomUUID());
    return     icesiUserRepository.save(icesiUserMapper.fromIcesiUserDTO(icesiUserDTO));

}




}
