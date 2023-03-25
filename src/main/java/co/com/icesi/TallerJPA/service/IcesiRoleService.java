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
    private final IcesiRoleRepository roleRepository;
    private final IcesiRoleMapper mapper;

    public IcesiRoleDTO save(IcesiRoleDTO dto){
        roleRepository.findByName(dto.getName()).ifPresent(
                e -> {throw new RuntimeException("This name's role already exists");}
        );
        IcesiRole role = mapper.fromRoleDTO(dto);
        role.setRoleId(UUID.randomUUID());
        role.setUsers(new ArrayList<>());
        return  mapper.fromIcesiRole(roleRepository.save(role));
    }

    public List<IcesiRoleDTO> getRoles(){
        return roleRepository.findAll().stream().map(mapper::fromIcesiRole).collect(Collectors.toList());
    }
}
