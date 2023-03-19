package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.mapper.IcesiUserMapper;

import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Builder
public class IcesiUserService {

    private final IcesiUserRepository icesiUserRepository;
    private final IcesiUserMapper icesiUserMapper;


    public IcesiUser saveUser(IcesiUserDto icesiUserToSave){
        return null;
    }
    //private final IcesiRoleRepository icesiRoleRepository;
   // private final IcesiRoleMapper icesiRoleMapper;

   /* public IcesiRole saveRole(IcesiRoleDto roleToSave){

        //Role name should be unique
        if(icesiRoleRepository.findByName(roleToSave.getName()).isPresent()){
            throw new RuntimeException("Role already taken");
        }else{
            //comvert from dto to actual role
            IcesiRole icesiRole = icesiRoleMapper.fromIcesiRoleDto(roleToSave);
            //Generate id
            icesiRole.setRoleId(UUID.randomUUID());
            return icesiRoleRepository.save(icesiRole);
        }
    }*/
}
