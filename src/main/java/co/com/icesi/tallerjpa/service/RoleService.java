package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RoleDTO;
import co.com.icesi.tallerjpa.error.enums.ErrorCode;
import co.com.icesi.tallerjpa.error.util.DetailBuilder;
import co.com.icesi.tallerjpa.mapper.RoleMapper;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static co.com.icesi.tallerjpa.error.util.ExceptionBuilder.createCustomException;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @SneakyThrows
    public RoleDTO save(RoleDTO roleDTO){

        boolean nameExists = roleRepository.existsByName(roleDTO.getName());

        if (nameExists){
            throw createCustomException(
                    "Name already exists",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_404)
            );
        }

        Role role = roleMapper.fromRoleDTO(roleDTO);
        role.setRoleId(UUID.randomUUID());
        return roleMapper.fromRole(roleRepository.save(role));

    }
}
