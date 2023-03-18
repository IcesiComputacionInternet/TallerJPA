package co.com.icesi.jpataller.service;

import co.com.icesi.jpataller.mapper.IcesiAccountMapper;
import co.com.icesi.jpataller.repository.IcesiAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IcesiAccountService {
    private final IcesiAccountMapper icesiAccountMapper;

    private final IcesiAccountRepository icesiAccountRepository;
}
