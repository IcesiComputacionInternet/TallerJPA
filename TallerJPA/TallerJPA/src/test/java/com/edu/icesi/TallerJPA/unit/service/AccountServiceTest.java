package com.edu.icesi.TallerJPA.unit.service;

import com.edu.icesi.TallerJPA.Enums.Scopes;
import com.edu.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.edu.icesi.TallerJPA.dto.IcesiTransactionDTO;
import com.edu.icesi.TallerJPA.mapper.AccountMapper;
import com.edu.icesi.TallerJPA.mapper.AccountMapperImpl;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.AccountRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import com.edu.icesi.TallerJPA.service.AccountService;
import com.edu.icesi.TallerJPA.unit.CustomJwtAuthenticationToken;
import com.edu.icesi.TallerJPA.unit.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;

    private AccountRepository accountRepository;

    private AccountMapper accountMapper;

    private UserRepository userRepository;

    @BeforeEach
    private void init() {
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        userRepository = mock(UserRepository.class);
        accountService = new AccountService(accountRepository, accountMapper, userRepository);
        setJwtMock();
    }

    private void setJwtMock() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("scope")).thenReturn(Scopes.ADMIN.toString());
        when(jwt.getClaimAsString("userId")).thenReturn(createIcesiUser().getUserId().toString());
        JwtAuthenticationToken jwtAuthenticationToken = new CustomJwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
    }

    @Test
    public void testCreateAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(createIcesiUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createIcesiUser()));

        accountService.save(createAccountDTO());

        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void testCreateAccountWithUserNotFound(){
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());

        try {
            accountService.save(createAccountDTO());
            fail();
        }catch (RuntimeException exception){
            String messageOfException = exception.getMessage();
            assertEquals("User not found", messageOfException);
        }

        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testCreateAccountWithBalanceBelowZero() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(createIcesiUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createIcesiUser()));

        try {
            accountService.save(createAccountDTOWithBalanceBelowZero());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Invalid balance", messageOfException);
        }
    }



    @Test
    public void testDisableExistingAccountWithBalanceGreaterThanZero() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.setToDisableState(createIcesiAccount().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("Invalid value", exception.getMessage());
        }
    }

    @Test
    public void testDisableAccountWithSameStatus() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createAccountDisable()));

        try {
            accountService.setToDisableState(createAccountDisable().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("Invalid change", exception.getMessage());
        }
    }

    @Test
    public void testEnableExistingAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createAccountDisable()));

        IcesiAccountDTO account = accountService.setToEnableState(createAccountDisable().getAccountNumber());

        verify(accountRepository, times(1)).save(any());

        assertTrue(account.isActive());
    }

    @Test
    public void testEnableAccountWithSameStatus() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.setToEnableState(createIcesiAccount().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("Invalid change", exception.getMessage());
        }
    }

    @Test
    public void testEnableNotExistingAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccountDTO accountCreateDTO = createAccountDTO();
        accountCreateDTO.setAccountNumber("13-154789-10");

        when(accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber())).thenReturn(Optional.empty());
        verify(accountRepository, times(0)).save(any());

        try {
            accountService.setToEnableState(accountCreateDTO.getAccountNumber());
            fail();

        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("ACCOUNT NOT FOUND", messageOfException);
        }

    }

    @Test
    public void testDisableNotExistingAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccountDTO accountCreateDTO = createAccountWithBalanceZeroDTO();
        accountCreateDTO.setAccountNumber("13-154789-10");

        when(accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber())).thenReturn(Optional.empty());
        verify(accountRepository, times(0)).save(any());

        try {
            accountService.setToDisableState(accountCreateDTO.getAccountNumber());
            fail();

        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("ACCOUNT NOT FOUND", messageOfException);
        }
    }

    @Test
    public void testWithdrawWithEnoughMoneyFromNormalAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createNormalAccount()));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(createNormalAccount().getAccountNumber(), "", 3000L, "",
                0L, 0L);

        transaction = accountService.withdrawals(transaction);

        verify(accountRepository, times(1)).save(any());

        assertEquals("Successful withdrawal", transaction.getResult());
        assertEquals(0L, transaction.getFinalBalanceSourceAccount());
    }

    @Test
    public void testWithdrawWithEnoughMoneyFromDepositOnlyAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createIcesiAccount()));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(createIcesiAccount().getAccountNumber(), "", 3000L, "",
                0L, 0L);

        transaction = accountService.withdrawals(transaction);

        verify(accountRepository, times(1)).save(any());

        assertEquals("Successful withdrawal", transaction.getResult());
        assertEquals(0L, transaction.getFinalBalanceSourceAccount());
    }

    @Test
    public void testWithdrawWithInsufficientMoneyFromNormalAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount account = createIcesiAccount();

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(account.getAccountNumber(), "", 8000L, "",
                0L, 0L);

        try {
            transaction = accountService.withdrawals(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testWithdrawWithInsufficientMoneyFromDepositOnlyAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount account = createIcesiAccount();

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(account.getAccountNumber(), "", 8000L, "",
                0L, 0L);

        try {
            transaction = accountService.withdrawals(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testWithdrawWithAccountNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccountDTO accountCreateDTO = createAccountDTO();
        accountCreateDTO.setAccountNumber("13-154789-10");

        when(accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber())).thenReturn(Optional.empty());

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(accountCreateDTO.getAccountNumber(), "", 3000L, "",
                0L, 0L);
        try {
            accountService.withdrawals(transaction);
            fail();

        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("ACCOUNT NOT FOUND", messageOfException);
        }

        verify(accountRepository, times(0)).save(any());

    }

    @Test
    public void testWithdrawWithInvalidValue() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount accountToDeposit = createNormalAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(accountToDeposit.getAccountNumber(), accountToDeposit.getAccountNumber(), 0L, "",
                0L, 0L);

        try {
            transaction = accountService.withdrawals(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }

        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testDepositMoneyNormalAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount accountToDeposit = createNormalAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(accountToDeposit.getAccountNumber(), accountToDeposit.getAccountNumber(), 3000L, "",
                0L, 0L);
        transaction = accountService.depositMoney(transaction);

        verify(accountRepository, times(1)).save(any());
        assertEquals("Successful deposit", transaction.getResult());
        assertEquals(0L, transaction.getFinalBalanceSourceAccount());
    }

    @Test
    public void testDepositInvalidMoneyNormalAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount accountToDeposit = createNormalAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(accountToDeposit.getAccountNumber(), accountToDeposit.getAccountNumber(), -1000L, "",
                0L, 0L);

        try {
            transaction = accountService.depositMoney(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }

        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testDepositMoneyDeposiT() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount accountToDeposit = createIcesiAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(accountToDeposit.getAccountNumber(), accountToDeposit.getAccountNumber(), 3000L, "",
                0L, 0L);

        transaction = accountService.depositMoney(transaction);

        verify(accountRepository, times(1)).save(any());
        assertEquals("Successful deposit", transaction.getResult());
        assertEquals(0L, transaction.getFinalBalanceSourceAccount());
    }

    @Test
    public void testDepositInvalidMoneyDeposit() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount accountToDeposit = createIcesiAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(accountToDeposit.getAccountNumber(), accountToDeposit.getAccountNumber(), -100L, "",
                0L, 0L);

        try {
            transaction = accountService.depositMoney(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }

        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testTransferMoneyToNormalAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount sourceAccount = createNormalAccount();
        IcesiAccount destinationAccount = createNormalAccount();

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), 3000L, "",
                0L, 0L);

        transaction = accountService.transferMoney(transaction);

        assertEquals("Successful transfer", transaction.getResult());
        assertEquals(2000L, transaction.getFinalBalanceSourceAccount());
        assertEquals(8000L, transaction.getFinalBalanceDestinationAccount());
    }

    @Test
    public void testTransferMoneyToNormalAccountFromInsufficientMoneyAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount sourceAccount = createNormalAccount();
        IcesiAccount destinationAccount = createNormalAccount();

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), 6000L, "",
                0L, 0L);

        try{
            transaction = accountService.transferMoney(transaction);
            fail();

        }catch (RuntimeException exception){
            assertEquals("Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
        verify(accountRepository, times(4)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyToNormalAccountWithInvalidValue() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount sourceAccount = createNormalAccount();
        IcesiAccount destinationAccount = createNormalAccount();

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), 0L, "",
                0L, 0L);

        try {
            transaction = accountService.transferMoney(transaction);
            fail();
        } catch (RuntimeException exception) {
            assertEquals("Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
        verify(accountRepository, times(4)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyToDepositOnlyAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount sourceDepositOnlyAccount = createIcesiAccount();
        IcesiAccount destinationAccount = createIcesiAccount();

        when(accountRepository.findByAccountNumber(sourceDepositOnlyAccount.getAccountNumber())).thenReturn(Optional.of(sourceDepositOnlyAccount));
        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(sourceDepositOnlyAccount.getAccountNumber(), destinationAccount.getAccountNumber(), 0L, "",
                0L, 0L);
        try {
            transaction = accountService.transferMoney(transaction);
            fail();
        } catch (RuntimeException exception) {
            assertEquals("Invalid account", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
        verify(accountRepository, times(4)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyFromDepositOnlyAccount() {
        when(userRepository.findById(any())).thenReturn(Optional.of(createIcesiUser()));

        IcesiAccount sourceAccount = createNormalAccount();
        IcesiAccount destinationDepositOnlyAccount = createIcesiAccount();

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(destinationDepositOnlyAccount.getAccountNumber())).thenReturn(Optional.of(destinationDepositOnlyAccount));

        IcesiTransactionDTO transaction = new IcesiTransactionDTO(sourceAccount.getAccountNumber(), destinationDepositOnlyAccount.getAccountNumber(), 0L, "",
                0L, 0L);

        try {
            transaction = accountService.transferMoney(transaction);
            fail();
        } catch (RuntimeException exception) {
            assertEquals("Invalid account", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
        verify(accountRepository, times(4)).findByAccountNumber(any());
    }

    public IcesiAccountDTO createAccountDTO() {
        return IcesiAccountDTO.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(5000)
                .type("Deposit only")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiAccountDTO createAccountWithBalanceZeroDTO() {
        return IcesiAccountDTO.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(0)
                .type("Deposit only")
                .icesiUser(createIcesiUser())
                .active(true)
                .build();
    }

    public IcesiAccount createAccountWithBalanceZero() {
        return IcesiAccount.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(0)
                .type("Deposit only")
                .icesiUser(createIcesiUser())
                .active(true)
                .build();
    }

    public IcesiAccount createNormalAccount() {
        return IcesiAccount.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(5000)
                .type("Normal")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiAccount createAccountDisable() {
        return IcesiAccount.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(5000)
                .type("Deposit Only")
                .active(false)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiAccountDTO createAccountDTOWithBalanceBelowZero() {
        return IcesiAccountDTO.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(-1)
                .type("Deposit only")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiAccount createIcesiAccount() {
        return IcesiAccount.builder()
                //.accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(5000)
                .type("Deposit only")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiUser createIcesiUser() {
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("1234567")
                .password("1234")
                .build();
    }
}