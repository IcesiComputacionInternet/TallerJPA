package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository irr;
    private final IcesiRoleMapper irm;
    public IcesiRole save(IcesiRoleCreateDTO d){
        if(irr.findByRoleName(d.getName()).isPresent()){
            throw new RuntimeException("The role name you are trying to enter already exists in the database.");
        }else{
            IcesiRole r = irm.fromIcesiRoleCreateDTO(d);
            r.setRoleId(UUID.randomUUID());
            r.setUsers(new ArrayList<>());
            return  irr.save(r);
        }
    }
}
