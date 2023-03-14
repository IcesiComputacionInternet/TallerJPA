package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            role.setUsers(new ArrayList<>());
            return  repository.save(role);
        }



    }

    public List<IcesiRoleDTO> getRoles(){
        return repository.findAll().stream().map(mapper::fromIcesiRole).collect(Collectors.toList());
    }
}
