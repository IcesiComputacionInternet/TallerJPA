package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.mapper.RoleMapper;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository icesiRoleRepository;
    private final RoleMapper icesiRoleMapper;
}
