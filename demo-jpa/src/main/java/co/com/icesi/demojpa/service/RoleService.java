package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.error.exception.DetailBuilder;
import co.com.icesi.demojpa.error.exception.ErrorCode;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static co.com.icesi.demojpa.error.util.IcesiExceptionBuilder.createIcesiException;


@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleCreateDTO save(RoleCreateDTO role){

        roleRepository.findByName(role.getName()).orElseThrow(
                createIcesiException(
                        "Role already exists",
                        HttpStatus.BAD_REQUEST,
                        new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Role", "Name", role.getName())
                ));


        IcesiRole icesiRole= roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        icesiRole.setUsers(new ArrayList<>());
        return roleMapper.fromIcesiRole(roleRepository.save(icesiRole));
    }


}
