package co.com.icesi.jpataller.service;

import co.com.icesi.jpataller.mapper.IcesiRoleMapper;
import co.com.icesi.jpataller.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IcesiRoleService {

    private final IcesiRoleRepository icesiRoleRepository;

    private final IcesiRoleMapper icesiRoleMapper;
}
