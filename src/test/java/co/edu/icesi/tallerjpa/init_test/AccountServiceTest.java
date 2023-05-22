package co.edu.icesi.tallerjpa.init_test;



import co.edu.icesi.tallerjpa.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.exception.*;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.service.IcesiAccountService;
import co.edu.icesi.tallerjpa.service.IcesiUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;



import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import javax.security.auth.login.AccountNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Mock
    private IcesiAccountMapper icesiAccountMapper;

    @Mock
    private IcesiAccountRepository icesiAccountRepository;

    @Mock
    private IcesiUserService icesiUserService;

    @Mock
    private IcesiUserRepository icesiUserRepository;

    @Mock
    private IcesiUserMapper icesiUserMapper;

    @InjectMocks
    private IcesiAccountService icesiAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() throws UserNotFoundException, NegativeBalanceException, DuplicateDataException, MissingParameterException {
        // Arrange
        IcesiAccountDTO accountDTO = new IcesiAccountDTO();
        accountDTO.setUserId(UUID.randomUUID().toString());
        accountDTO.setBalance((long) 100.0);

        UUID userId = UUID.fromString(accountDTO.getUserId());
        IcesiUser user = new IcesiUser();
        user.setId(userId);

        when(icesiUserRepository.existsById(userId)).thenReturn(true);
        when(icesiUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(icesiAccountMapper.toIcesiAccount(accountDTO)).thenReturn(new IcesiAccount());
        //when(icesiUserMapper.fromModel(user)).thenReturn(new IcesiUserDTO());
        when(icesiAccountRepository.save(any(IcesiAccount.class))).thenReturn(new IcesiAccount());

        // Act
        IcesiAccount result = icesiAccountService.createAccount(accountDTO);

        // Assert
        Assertions.assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    void testDisableAccount() throws AccountBalanceNotZeroException, AccountNotFoundException {
        // Arrange
        String accountNumber = "123-456789-00";
        IcesiAccount account = new IcesiAccount();
        account.setBalance(0);
        account.setActive(true);

        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(icesiAccountRepository.save(any(IcesiAccount.class))).thenReturn(new IcesiAccount());

        // Act
        String result = icesiAccountService.disableAccount(accountNumber);

        // Assert
        Assertions.assertEquals(accountNumber, result);
        Assertions.assertFalse(account.getActive());
        // Add more assertions as needed
    }

    @Test
    void testEnableAccount() {
        // Arrange
        String accountNumber = "123-456789-00";
        IcesiAccount account = new IcesiAccount();
        account.setActive(false);

        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(icesiAccountRepository.save(any(IcesiAccount.class))).thenReturn(new IcesiAccount());

        // Act
        String result = icesiAccountService.enableAccount(accountNumber);

        // Assert
        Assertions.assertEquals(accountNumber, result);
        Assertions.assertTrue(account.getActive());
        // Add more assertions as needed
    }

    @Test
    void testWithdrawMoney() {
        // Arrange
        String accountNumber = "123-456789-00";
        long amount = 50;
        IcesiAccount account = new IcesiAccount();
        account.setActive(true);
        account.setBalance(100);

        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(icesiAccountRepository.save(any(IcesiAccount.class))).thenReturn(new IcesiAccount());

        // Act
        String result = icesiAccountService.withdrawMoney(accountNumber, amount);

        // Assert
        Assertions.assertEquals(accountNumber, result);
        Assertions.assertEquals(50, account.getBalance());
        // Add more assertions as needed
    }

    @Test
    void testDepositMoney() {
        // Arrange
        String accountNumber = "123-456789-00";
        long amount = 50;
        IcesiAccount account = new IcesiAccount();
        account.setActive(true);
        account.setBalance(100);

        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(icesiAccountRepository.save(any(IcesiAccount.class))).thenReturn(new IcesiAccount());

        // Act
        String result = icesiAccountService.depositMoney(accountNumber, amount);

        // Assert
        Assertions.assertEquals(accountNumber, result);
        Assertions.assertEquals(150, account.getBalance());
        // Add more assertions as needed
    }
}

