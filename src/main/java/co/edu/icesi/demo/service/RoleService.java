package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.RoleCreateDTO;
import co.edu.icesi.demo.error.exception.DetailBuilder;
import co.edu.icesi.demo.error.exception.ErrorCode;
import co.edu.icesi.demo.mapper.RoleMapper;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import static co.edu.icesi.demo.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    private RoleMapper roleMapper;

    public RoleCreateDTO save(RoleCreateDTO role){ //Only admin
        if(roleRepository.findByName(role.getName()).isPresent()){

            throw createIcesiException(
                    "Role name already exists",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Role name",role.getName() )
            ).get();


        }

        IcesiRole icesiRole=roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());

        return roleMapper.fromIcesiRole(roleRepository.save(icesiRole));
    }


}
