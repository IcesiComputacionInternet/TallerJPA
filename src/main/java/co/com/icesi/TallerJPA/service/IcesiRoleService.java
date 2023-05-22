package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository roleRepository;
    private final IcesiRoleMapper roleMapper;

    public IcesiRoleCreateDTO save(IcesiRoleCreateDTO roleDTO){
        if(roleRepository.findByName(roleDTO.getName()).isPresent()){
            throw new RuntimeException("Role name already exists in the database: "+ roleDTO.getName());
        }
        IcesiRole role = roleMapper.fromIcesiRoleDTO(roleDTO);
        role.setRoleId(UUID.randomUUID());
        return roleMapper.fromIcesiRole(roleRepository.save(role));
    }
}
