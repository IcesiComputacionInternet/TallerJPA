package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.mapper.IcesiRoleMapper;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
@Builder
public class IcesiRoleService {

    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiRoleMapper icesiRoleMapper;

    public IcesiRole saveRole(IcesiRoleDto roleToSave){

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
    }
}
