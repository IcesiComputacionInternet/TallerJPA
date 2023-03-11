package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository repository;
    private final IcesiRoleMapper mapper;
}
