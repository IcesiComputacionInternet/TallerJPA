package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository repository;
    private final IcesiRoleMapper mapper;

    public IcesiRole save(IcesiRoleDTO dto){

        if(repository.findByName(dto.getName()).isPresent()){
            throw new RuntimeException("This name's role already exists");
        }else{
            IcesiRole role = mapper.fromRoleDTO(dto);
            role.setRoleId(UUID.randomUUID());
            return  repository.save(role);
        }



    }
}
