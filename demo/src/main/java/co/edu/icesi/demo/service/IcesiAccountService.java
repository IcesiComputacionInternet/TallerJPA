package co.edu.icesi.demo.service;

import co.edu.icesi.demo.mapper.IcesiAccountMapper;
import co.edu.icesi.demo.repository.IcesiAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountRepository icesiAccountRepository;
    private final IcesiAccountMapper icesiAccountMapper;
}
