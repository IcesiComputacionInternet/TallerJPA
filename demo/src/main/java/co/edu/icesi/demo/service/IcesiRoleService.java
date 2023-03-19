package co.edu.icesi.demo.service;

import co.edu.icesi.demo.mapper.IcesiRoleMapper;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IcesiRoleService {

    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiRoleMapper icesiRoleMapper;
}
