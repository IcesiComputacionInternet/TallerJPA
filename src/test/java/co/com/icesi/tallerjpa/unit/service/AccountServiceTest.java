package co.com.icesi.tallerjpa.unit.service;


import co.com.icesi.tallerjpa.Enum.AccountType;
import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.mapper.AccountMapperImpl;
import co.com.icesi.tallerjpa.model.IcesiAccount;
import co.com.icesi.tallerjpa.model.IcesiRole;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private UserRepository userRepository;

    @BeforeEach
    public void init(){
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        userRepository = mock(UserRepository.class);
        accountService = new AccountService(accountRepository, accountMapper, userRepository);
        userRepository.save(defaultIcesiUser());
    }

    @Test
    public void testCreateAccount(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        accountService.save(accountCreateDTO());
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(defaultIcesiAccount())));
        verify(accountMapper, times(1)).fromIcesiAccountDTO(any());
    }

    @Test
    public void checkAccountNumber(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountCreateDTO());

        verify(accountRepository,times( 1)).save(argThat(accountNum-> accountNum.getAccountNumber().matches("[0-9]{3}-[0-9]{6}-[0-9]{2}")));
    }

    @Test
    public void testCreateAccountWithBalanceBelowZero(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try {
            accountService.save(defaultAccountCreateDTOBalanceBelowZero());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Account balance can't be below 0", messageOfException);
        }
    }

    @Test
    public void testCreateAccountWithNoUser(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        try {
            accountService.save(defaultAccountCreateDTO());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("There is no user for the account", messageOfException);
        }
    }

    @Test
    public void testCantDisableAccount() {
        IcesiAccount icesiAccount = defaultIcesiAccount();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(defaultIcesiAccount()));

        try {
            accountService.disableAccount(icesiAccount.getAccountNumber());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Account can't be disabled", messageOfException);
        }
    }

    @Test
    public void testDisableAccount() {
        IcesiAccount icesiAccount = IcesiAccount.builder()
                    .accountNumber("123-456789-10")
                    .balance(0)
                    .type(AccountType.DEPOSIT_ONLY.toString())
                    .active(true)
                    .user(defaultIcesiUser())
                    .build();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));

        ResponseAccountDTO responseAccountDTO = accountService.disableAccount(icesiAccount.getAccountNumber());
        assertFalse(responseAccountDTO.isActive());
    }

    @Test
    public void testCantEnableAccount() {
        IcesiAccount icesiAccount = defaultIcesiAccount();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(defaultIcesiAccount()));

        try {
            accountService.enableAccount(icesiAccount.getAccountNumber());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Account can't be enabled", messageOfException);
        }
    }

    @Test
    public void testEnableAccount() {
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));

        ResponseAccountDTO responseAccountDTO = accountService.enableAccount(icesiAccount.getAccountNumber());
        assertTrue(responseAccountDTO.isActive());

    }

    @Test
    public void testWithdrawalMoneySuccessfully(){

        IcesiAccount icesiAccount = defaultIcesiAccount();
        when(accountRepository.save(any())).thenReturn(icesiAccount);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));

        TransactionDTO transactionDTO = accountService.withdrawal(defaultTransactionDTO());

        assertEquals(11000, icesiAccount.getBalance());
        assertEquals("Successful withdrawal", transactionDTO.getResult());
    }

    //cuenta no existe

    @Test
    public void testWithdrawalBalanceLessThanAmount(){

        IcesiAccount icesiAccount = IcesiAccount.builder()
                .accountNumber("123-456789-10")
                .balance(500)
                .type(AccountType.DEPOSIT_ONLY.toString())
                .active(true)
                .user(defaultIcesiUser())
                .build();
        when(accountRepository.save(any())).thenReturn(icesiAccount);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));

        try {
            accountService.withdrawal(defaultTransactionDTO());
            //fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("The amount of money in Balance is less than the amount needed", messageOfException);
        }
    }

    @Test
    public void testWithdrawalAccountNotActive(){

        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);
        when(accountRepository.save(any())).thenReturn(icesiAccount);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));

        try {
            accountService.withdrawal(defaultTransactionDTO());
            //fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Unsuccessful withdrawal", messageOfException);
        }
    }

    @Test
    public void testDepositSuccessfully(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        when(accountRepository.save(any())).thenReturn(icesiAccount);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));

        TransactionDTO transactionDTO = accountService.deposit(defaultTransactionDTO());

        assertEquals(13000, icesiAccount.getBalance());
        assertEquals("Successful deposit", transactionDTO.getResult());

    }

    @Test
    public void testDepositAccountNotActive(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);

        when(accountRepository.save(any())).thenReturn(icesiAccount);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));

        try {
            accountService.deposit(defaultTransactionDTO());
            //fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Unsuccessful deposit", messageOfException);
        }
    }

    @Test
    public void testTransferSuccessfully(){
        IcesiAccount accountFrom = defaultIcesiAccount();
        accountFrom.setType(AccountType.NORMAL.toString());

        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("123-456789-34")
                .balance(1000)
                .type(AccountType.NORMAL.toString())
                .active(true)
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findByAccountNumber("123-456789-10")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByAccountNumber("123-456789-34")).thenReturn(Optional.of(accountTo));

        TransactionDTO transactionDTO = accountService.transfer(defaultTransactionDTO2());

        assertEquals(10000, accountFrom.getBalance());
        assertEquals(3000, accountTo.getBalance());
        assertEquals("Successful transfer",transactionDTO.getResult());

    }

    @Test
    public void testTransferAccountToNotActive(){
        IcesiAccount accountFrom = defaultIcesiAccount();
        accountFrom.setType(AccountType.NORMAL.toString());

        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("123-456789-34")
                .balance(1000)
                .type(AccountType.NORMAL.toString())
                .active(false)
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findByAccountNumber("123-456789-10")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByAccountNumber("123-456789-34")).thenReturn(Optional.of(accountTo));

        try {
            accountService.transfer(defaultTransactionDTO2());
            //fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Unsuccessful transfer", messageOfException);
        }
    }

    @Test
    public void testTransferAccountsNotActive(){
        IcesiAccount accountFrom = defaultIcesiAccount();
        accountFrom.setType(AccountType.NORMAL.toString());
        accountFrom.setActive(false);

        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("123-456789-34")
                .balance(1000)
                .type(AccountType.NORMAL.toString())
                .active(false)
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findByAccountNumber("123-456789-10")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByAccountNumber("123-456789-34")).thenReturn(Optional.of(accountTo));

        try {
            accountService.transfer(defaultTransactionDTO2());
            //fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Unsuccessful transfer", messageOfException);
        }
    }

    @Test
    public void testTransferAccountDeposit(){
        IcesiAccount accountFrom = defaultIcesiAccount();

        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("123-456789-34")
                .balance(1000)
                .type(AccountType.NORMAL.toString())
                .active(true)
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findByAccountNumber("123-456789-10")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByAccountNumber("123-456789-34")).thenReturn(Optional.of(accountTo));

        try {
            accountService.transfer(defaultTransactionDTO2());
            //fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("The account's type is DEPOSIT_ONLY", messageOfException);
        }
    }

    @Test
    public void testTransferAccountsDeposit(){
        IcesiAccount accountFrom = defaultIcesiAccount();
        accountFrom.setType(AccountType.DEPOSIT_ONLY.toString());

        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("123-456789-34")
                .balance(1000)
                .type(AccountType.DEPOSIT_ONLY.toString())
                .active(true)
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findByAccountNumber("123-456789-10")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByAccountNumber("123-456789-34")).thenReturn(Optional.of(accountTo));

        try {
            accountService.transfer(defaultTransactionDTO2());
            //fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("The account's type is DEPOSIT_ONLY", messageOfException);
        }
    }


    @Test
    public void testTransferAccountFromNotEnoughBalance(){
        IcesiAccount accountFrom = defaultIcesiAccount();
        accountFrom.setType(AccountType.NORMAL.toString());
        accountFrom.setBalance(10);

        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("123-456789-34")
                .balance(1000)
                .type(AccountType.NORMAL.toString())
                .active(true)
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findByAccountNumber("123-456789-10")).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByAccountNumber("123-456789-34")).thenReturn(Optional.of(accountTo));

        try {
            accountService.transfer(defaultTransactionDTO2());
            //fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("The amount of money in Balance is less than the amount needed", messageOfException);
        }
    }


    private TransactionDTO defaultTransactionDTO2(){
        return TransactionDTO.builder()
                .accountFrom("123-456789-10")
                .accountTo("123-456789-34")
                .amount(2000L)
                .result("")
                .build();
    }


    private TransactionDTO defaultTransactionDTO(){
        return TransactionDTO.builder()
                .accountFrom("123-456789-10")
                .accountTo("")
                .amount(1000L)
                .result("")
                .build();
    }

    private RequestAccountDTO defaultAccountCreateDTOBalanceBelowZero(){
        return RequestAccountDTO.builder()
                .balance(-200L)
                .type(AccountType.DEPOSIT_ONLY)
                .user("testEmail2@example.com")
                .build();
    }

    private RequestAccountDTO accountCreateDTO(){
        return RequestAccountDTO.builder()
                .balance(12000L)
                .type(AccountType.DEPOSIT_ONLY)
                .user("testEmail2@example.com")
                .build();
    }

    private RequestAccountDTO defaultAccountCreateDTO(){
        return RequestAccountDTO.builder()
                .balance(200123L)
                .type(AccountType.DEPOSIT_ONLY)
                .user("testEmail2@example.com")
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("123-456789-10")
                .balance(12000L)
                .type(AccountType.DEPOSIT_ONLY.toString())
                .active(true)
                .user(defaultIcesiUser())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("testEmail@example.com")
                .phoneNumber("1234567")
                .password("1234")
                .role(defaultIcesiRole())
                .build();
    }

    /*private UserCreateDTO defaultUserCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .email("testEmail@example.com")
                .phoneNumber("1234567")
                .password("1234")
                .icesiroleDto(defaultRoleCreateDTO())
                .build();
    }*/

    /*private RoleCreateDTO defaultRoleCreateDTO() {
        return RoleCreateDTO.builder()
                .description("no description")
                .name("Administrator")
                .build();
    }*/

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("no description")
                .name("Administrator")
                .build();
    }



}
