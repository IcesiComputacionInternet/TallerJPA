package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.IcesiAccountDto;
import co.edu.icesi.demo.mapper.IcesiAccountMapper;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.repository.IcesiAccountRepository;
import co.edu.icesi.demo.service.IcesiAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class IcesiAccountTest {

    private IcesiAccountService icesiAccountService;

    private IcesiAccountRepository icesiAccountRepository;

    private IcesiAccountMapper icesiAccountMapper;

    @BeforeEach
    public void setup(){
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        icesiAccountMapper = spy(IcesiAccountMapper.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, icesiAccountMapper);
    }

    private IcesiAccount createDefaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("123-456789-00")
                .balance(50)
                .type("deposit")
                .active(true)
                .build();
    }

    private IcesiAccountDto createDefaultIcesiAccountDto(){
        return IcesiAccountDto.builder()
                .accountNumber("123-456789-00")
                .balance(50)
                .type("deposit")
                .active(true)
                .build();
    }

    @Test
    public void testSaveAccount(){
        icesiAccountService.saveAccount(createDefaultIcesiAccountDto());
        IcesiAccount icesiAccount = createDefaultIcesiAccount();

        verify(icesiAccountRepository, times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
    }
}
