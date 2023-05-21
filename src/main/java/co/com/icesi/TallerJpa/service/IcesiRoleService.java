package co.com.icesi.TallerJpa.service;

import co.com.icesi.TallerJpa.dto.IcesiRoleDTO;
import co.com.icesi.TallerJpa.error.exception.DetailBuilder;
import co.com.icesi.TallerJpa.error.exception.ErrorCode;
import co.com.icesi.TallerJpa.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.com.icesi.TallerJpa.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiRoleMapper icesiRoleMapper;

    public IcesiRoleDTO saveRole(IcesiRoleDTO roleDto) {
        if(icesiRoleRepository.existsByName(roleDto.name())){
            throw createIcesiException(
                    "The name "+roleDto.name()+" is already in use",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED,"IcesiRole","name", roleDto.name())
            ).get();
        }

        IcesiRole icesiRole = icesiRoleMapper.fromRoleDto(roleDto);
        icesiRole.setRoleId(UUID.randomUUID());

        return icesiRoleMapper.fromIcesiRole(icesiRoleRepository.save(icesiRole));
    }

    public List<IcesiRoleDTO> saveListRoles(List<IcesiRoleDTO> icesiRoleDTOS){
        return icesiRoleDTOS.stream().map(this::saveRole).collect(Collectors.toList());
    }

    public IcesiRoleDTO getRoleByName(String roleName){
        return icesiRoleMapper.fromIcesiRole(icesiRoleRepository.findByName(roleName).orElse(null));
    }

    public List<IcesiRoleDTO> getAllRoles(){
        return icesiRoleRepository.findAll().stream()
                .map(icesiRoleMapper::fromIcesiRole).collect(Collectors.toList());
    }
}
