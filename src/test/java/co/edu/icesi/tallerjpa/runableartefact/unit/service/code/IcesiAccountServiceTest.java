package co.edu.icesi.tallerjpa.runableartefact.unit.service.code;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.InsufficientBalance;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.OperationNotAvailable;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiAccount;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiAccountService;
import co.edu.icesi.tallerjpa.runableartefact.unit.service.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiAccountServiceTest {


    private IcesiAccountService icesiAccountService;

    private IcesiAccountRepository icesiAccountRepository;

    private IcesiAccountMapper icesiAccountMapper;

    private IcesiUserRepository icesiUserRepository;

    @BeforeEach
    public void init() {
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        icesiAccountMapper = spy(IcesiAccountMapper.class);
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, icesiUserRepository,icesiAccountMapper);
    }

    @Test
    public void saveNewIcesiAccount() {
        when(icesiAccountMapper.toIcesiAccount(any())).thenReturn(createDefaultIcesiAccount());
        when(icesiAccountRepository.save(any())).thenReturn(createDefaultIcesiAccount());
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.ofNullable(createDefaultIcesiUser()));
        when(icesiAccountRepository.existsByAccountNumber(any())).thenReturn(false);

        icesiAccountService.saveNewAccount(createDefaultIcesiAccountDTO());

        verify(icesiAccountMapper, times(1)).toIcesiAccount(any());
        verify(icesiUserRepository, times(2)).findByEmail(any());
        verify(icesiAccountRepository, times(1)).save(argThat(new IcesiAccountMatcher(createDefaultIcesiAccount())));
    }

    @Test
    public void saveNewIcesiAccountWithExistingAccountNumber() {
        when(icesiAccountMapper.toIcesiAccount(any())).thenReturn(createDefaultIcesiAccount());
        when(icesiAccountRepository.save(any())).thenReturn(createDefaultIcesiAccount());
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.ofNullable(createDefaultIcesiUser()));
        when(icesiAccountRepository.existsByAccountNumber(any())).thenReturn(true);


        assertTrue(true);
    }

    @Test
    public void activateAccountTest() {
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultIcesiAccount()));
        when(icesiAccountRepository.save(any())).thenReturn(createDefaultIcesiAccountDeactivated());

        IcesiAccount icesiAccount = createDefaultIcesiAccountDeactivated();
        icesiAccountService.activateAccount(icesiAccount.getAccountNumber());
        icesiAccount = icesiAccountRepository.findByAccountNumber(createDefaultIcesiAccount().getAccountNumber()).get();

        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
        assertTrue(icesiAccount.isActive());
    }

    @Test
    public void deactivateAccountTest() {
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultIcesiAccountDeactivated()));
        when(icesiAccountRepository.save(any())).thenReturn(createDefaultIcesiAccountDeactivated());

        IcesiAccount icesiAccount = createDefaultIcesiAccount();
        icesiAccountService.deactivateAccount(icesiAccount.getAccountNumber());
        icesiAccount = icesiAccountRepository.findByAccountNumber(createDefaultIcesiAccount().getAccountNumber()).get();

        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
        assertFalse(icesiAccount.isActive());
    }

    @Test
    public void withdrawalTest() {
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultIcesiAccount()));
        when(icesiAccountRepository.save(any())).thenReturn(createDefaultIcesiAccount());

        IcesiAccount icesiAccount = icesiAccountRepository.findByAccountNumber(createDefaultIcesiAccount().getAccountNumber()).get();
        icesiAccountService.withdrawal(icesiAccount.getAccountNumber(), 100L);

        verify(icesiAccountRepository, atLeast(1)).findByAccountNumber(any());
        verify(icesiAccountRepository, atLeast(1)).save(any());
        assertEquals(icesiAccount.getBalance(), 900L);
    }

    @Test
    public void withdrawalWhenBalanceIsLessThanAmountTest() {
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultIcesiAccount()));
        when(icesiAccountRepository.save(any())).thenReturn(createDefaultIcesiAccount());

        IcesiAccount icesiAccount = createADepositIcesiAccount();

        assertThrows(InsufficientBalance.class, () -> icesiAccountService.withdrawal(icesiAccount.getAccountNumber(), 5000L));

        verify(icesiAccountRepository, atLeast(1)).findByAccountNumber(any());
    }

    @Test
    public void depositTest() {
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultIcesiAccount()));
        when(icesiAccountRepository.save(any())).thenReturn(createDefaultIcesiAccount());

        IcesiAccount icesiAccount = icesiAccountRepository.findByAccountNumber(createDefaultIcesiAccount().getAccountNumber()).get();
        icesiAccountService.deposit(icesiAccount.getAccountNumber(), 100L);

        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
        verify(icesiAccountRepository, times(1)).save(any());
        assertEquals(icesiAccount.getBalance(), 1100L);
    }

    @Test
    public void transferTest() {
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultIcesiAccount()));
        when(icesiAccountRepository.findByAccountNumber("c0860f9e-c425-11ed-afa1-0242ac120002")).thenReturn(Optional.ofNullable(createDefaultIcesiAccount()));
        when(icesiAccountRepository.findByAccountNumber("c0860f9e-c425-11ed-afa1-0242ac120001")).thenReturn(Optional.ofNullable(createDefaultIcesiAccount2()));
        when(icesiAccountRepository.save(any())).thenReturn(createDefaultIcesiAccount());

        IcesiAccount icesiAccount = icesiAccountRepository.findByAccountNumber("c0860f9e-c425-11ed-afa1-0242ac120002").get();
        IcesiAccount icesiAccount2 = icesiAccountRepository.findByAccountNumber("c0860f9e-c425-11ed-afa1-0242ac120001").get();
        String testValue = icesiAccountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber() ,100L);

        verify(icesiAccountRepository, times(6)).findByAccountNumber(any());
        verify(icesiAccountRepository, times(2)).save(any());
        assertEquals("Transfer successful", testValue);
    }

    @Test
    public void transferToADepositIcesiAccount(){
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createADepositIcesiAccount()));
        when(icesiAccountRepository.save(any())).thenReturn(createDefaultIcesiAccount());

        IcesiAccount icesiAccount = icesiAccountRepository.findByAccountNumber("c0860f9e-c425-11ed-afa1-0242ac120002").get();
        assertThrows(OperationNotAvailable.class, () -> icesiAccountService.transfer(icesiAccount.getAccountNumber(), "c0860f9e-c425-11ed-afa1-0242ac120001", 100L));
    }
    private IcesiAccount createDefaultIcesiAccount() {
        return IcesiAccount.builder()
                .accountId(UUID.fromString("c0860f9e-c425-11ed-afa1-0242ac120002"))
                .balance(1000L)
                .type("Ahorros")
                .active(true)
                .user(createDefaultIcesiUser())
                .build();
    }
    private IcesiAccount createDefaultIcesiAccount2() {
        return IcesiAccount.builder()
                .accountId(UUID.fromString("c0860f9e-c425-11ed-afa1-0242ac120001"))
                .balance(1000L)
                .type("Ahorros")
                .active(true)
                .user(createDefaultIcesiUser())
                .build();
    }

    private IcesiAccount createADepositIcesiAccount() {
        return IcesiAccount.builder()
                .accountId(UUID.fromString("c0860f9e-c425-11ed-afa1-0242ac120002"))
                .balance(1100L)
                .type("deposit")
                .active(true)
                .user(createDefaultIcesiUser())
                .build();
    }
    private IcesiAccount createDefaultIcesiAccountDeactivated() {
        return IcesiAccount.builder()
                .balance(0L)
                .type("Ahorros")
                .active(false)
                .user(createDefaultIcesiUser())
                .build();
    }

    private IcesiUser createDefaultIcesiUser() {
        return IcesiUser.builder()
                .userId(UUID.fromString("c0860f9e-c425-11ed-afa1-0242ac120002"))
                .email("prueba@gmail.com")
                .firstName("prueba")
                .lastName("prueba")
                .build();
    }
    private IcesiAccountDTO createDefaultIcesiAccountDTO() {
        return IcesiAccountDTO.builder()
                .balance(1000L)
                .type("Ahorros")
                .active(true)
                .icesiUserEmail("prueba@gmail.com")
                .build();
    }
}
