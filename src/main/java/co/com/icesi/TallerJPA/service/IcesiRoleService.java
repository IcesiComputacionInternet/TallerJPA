package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.Enum.Action;
import co.com.icesi.TallerJPA.dto.ActionResultDTO;
import co.com.icesi.TallerJPA.dto.AssingRoleDTO;
import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository roleRepository;
    private final IcesiRoleMapper mapper;
    private final IcesiUserRepository userRepository;

    public IcesiRoleDTO save(IcesiRoleDTO dto){
        roleRepository.findByName(dto.getName()).ifPresent(
                e -> {throw new RuntimeException("This name's role already exists");}
        );
        IcesiRole role = mapper.fromRoleDTO(dto);
        role.setRoleId(UUID.randomUUID());
        role.setUsers(new ArrayList<>());
        return  mapper.fromIcesiRole(roleRepository.save(role));
    }

    @Transactional
    public ActionResultDTO assingRole(AssingRoleDTO dto){
        IcesiRole roleToAssing = roleRepository.findByName(dto.getRoleToAssing()).orElseThrow(() -> new RuntimeException("This role was not found"));
        IcesiUser userToBeAssing = userRepository.findByEmail(dto.getEmailOfUser()).orElseThrow(() -> new RuntimeException("The user does not exits"));
        userToBeAssing.setRole(roleToAssing);
        userRepository.save(userToBeAssing);
        return actionFinished("Role assigned to user with email: "+dto.getEmailOfUser());
    }

    public List<IcesiRoleDTO> getRoles(){
        return roleRepository.findAll().stream().map(mapper::fromIcesiRole).collect(Collectors.toList());
    }

    private ActionResultDTO actionFinished(String actionPerformed){
        return  ActionResultDTO.builder().type(Action.UPDATE).actionPerformed(actionPerformed).build();
    }

    
}
