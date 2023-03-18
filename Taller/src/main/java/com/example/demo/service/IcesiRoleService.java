package com.example.demo.service;

import com.example.demo.DTO.IcesiRoleDTO;
import com.example.demo.Mapper.IcesiRoleMapper;
import com.example.demo.Repository.IcesiRoleRepository;
import com.example.demo.model.IcesiRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class IcesiRoleService {

    @Autowired
    private final IcesiRoleRepository roleRepository;

    private final IcesiRoleMapper roleMapper;

    public IcesiRole createIcesiRole(IcesiRoleDTO roleDTO) {
        roleDTO.setRoleId(UUID.randomUUID());
        return roleRepository.save(roleMapper.fromIcesiRoleDTO(roleDTO));
    }
}
