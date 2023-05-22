package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.RoleDTO;
import co.edu.icesi.demo.error.exception.DetailBuilder;
import co.edu.icesi.demo.error.exception.ErrorCode;
import co.edu.icesi.demo.mapper.RoleMapper;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static co.edu.icesi.demo.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    private RoleMapper roleMapper;

    public RoleDTO save(RoleDTO role){ //Only admin
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

    public RoleDTO getRole(String roleName) {
        return  roleMapper.fromIcesiRole(roleRepository.findByName(roleName).orElseThrow(
                createIcesiException(
                        "Role name not found",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "Role name",roleName )
                )
        ));
    }

    public List<RoleDTO> getAllRoles() {

        return roleRepository.findAll().stream()
                .map(roleMapper::fromIcesiRole)
                .toList();

    }


}
