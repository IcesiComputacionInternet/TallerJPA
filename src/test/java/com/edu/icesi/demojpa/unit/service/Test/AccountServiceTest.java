package com.edu.icesi.demojpa.unit.service.Test;

import com.edu.icesi.demojpa.Enum.AccountType;
import com.edu.icesi.demojpa.dto.request.RequestAccountDTO;
import com.edu.icesi.demojpa.dto.request.RequestTransactionDTO;
import com.edu.icesi.demojpa.dto.response.ResponseAccountDTO;
import com.edu.icesi.demojpa.dto.response.ResponseTransactionDTO;
import com.edu.icesi.demojpa.error.exception.IcesiException;
import com.edu.icesi.demojpa.error.util.IcesiExceptionBuilder;
import com.edu.icesi.demojpa.mapper.AccountMapper;
import com.edu.icesi.demojpa.mapper.AccountMapperImpl;
import com.edu.icesi.demojpa.model.IcesiAccount;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.model.IcesiUser;
import com.edu.icesi.demojpa.repository.AccountRepository;
import com.edu.icesi.demojpa.repository.UserRepository;
import com.edu.icesi.demojpa.service.AccountService;
import com.edu.icesi.demojpa.unit.service.Matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private AccountService accountService;
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountMapper accountMapper;
    private final AccountType depositOnly = AccountType.DEPOSIT_ONLY;
    private final AccountType normalAccount = AccountType.NORMAL;

    @BeforeEach
    private void init() {
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        accountService = new AccountService(accountRepository, userRepository, accountMapper);
    }

    @Test
    public void testCreateAccount() {
        when(userRepository.findUserById(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));

        ResponseAccountDTO icesiAccount = accountService.save(defaultAccountDTO());
        IcesiAccount icesiAccountToCompare = defaultIcesiAccount();

        assertTrue(isCorrectFormat(icesiAccount.getAccountNumber()));
        verify(userRepository, times(1)).findUserById(any());
        verify(accountMapper, times(1)).fromIcesiAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new IcesiAccountMatcher(icesiAccountToCompare)));
    }

    private boolean isCorrectFormat(String accountNumber) {
        return accountNumber.matches("^[0-9]{3}-[0-9]{6}-[0-9]{2}$");
    }

    @Test
    public void testEnableAccount() {
        IcesiAccount account = defaultIcesiAccount();
        RequestAccountDTO requestAccountDTO = defaultAccountDTO();
        account.setBalance(0L);
        account.setActive(false);

        when(accountRepository.findAccountByAccountNumber(account.getAccountNumber(), true)).thenReturn(Optional.of(account));
        when(accountRepository.isOwnerAccount(requestAccountDTO.getIcesiUserId(), requestAccountDTO.getAccountNumber())).thenReturn(true);

        ResponseAccountDTO response = accountService.enableAccount(requestAccountDTO);

        verify(accountRepository, times(1)).findAccountByAccountNumber("000-000000-00", true);
        verify(accountRepository, times(1)).save(any());
        verify(accountMapper, times(1)).fromAccountDTO(any(), any());
        assertTrue(account.isActive());
        assertEquals("The account has been activated", response.getResult());
    }

    @Test
    public void testEnableNotFoundAccount() {
        IcesiAccount account = defaultIcesiAccount();
        RequestAccountDTO requestAccountDTO = defaultAccountDTO();
        account.setActive(false);

        when(accountRepository.findAccountByAccountNumber(account.getAccountNumber(), true)).thenReturn(Optional.empty());
        when(accountRepository.isOwnerAccount(requestAccountDTO.getIcesiUserId(), requestAccountDTO.getAccountNumber())).thenReturn(true);

        try {
            accountService.enableAccount(requestAccountDTO);
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("Account with number 000-000000-00 can't be enabled", message);
            assertFalse(account.isActive());
            verify(accountRepository, times(1)).findAccountByAccountNumber("000-000000-00", true);
        }
    }

    @Test
    public void testDisableAccount() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(0L);

        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.of(account));

        ResponseAccountDTO response = accountService.disableAccount(defaultAccountDTO());

        verify(accountRepository, times(1)).findAccountByAccountNumber("000-000000-00", true);
        verify(accountRepository, times(1)).save(any());
        verify(accountMapper, times(1)).fromAccountDTO(any(), any());
        assertFalse(account.isActive());
        assertEquals("The account has been disabled", response.getResult());
    }

    @Test
    public void testDisableNotFoundAccount() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(0L);
        account.setActive(true);

        when(accountRepository.findAccountByAccountNumber(account.getAccountNumber(), true)).thenReturn(Optional.empty());

        try {
            accountService.disableAccount(defaultAccountDTO());
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("Account with number 000-000000-00 can't be disabled", message);
            assertTrue(account.isActive());
            verify(accountRepository, times(1)).findAccountByAccountNumber("000-000000-00", true);
        }
    }

    @Test
    public void testDisableAccountWithBalance() {
        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.of(defaultIcesiAccount()));

        try {
            accountService.disableAccount(defaultAccountDTO());
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("The account couldn't be deactivated because it is funded", message);
        }
    }

    @Test
    public void testWithdraw() {
        IcesiAccount icesiAccount = defaultIcesiAccount();

        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.ofNullable(icesiAccount));

        ResponseTransactionDTO withdraw = accountService.withdraw(defaultTransactionDTO());

        assertEquals(50L, getBalance(icesiAccount));
        assertEquals("The withdrawal was successfully carried out", withdraw.getResult());
    }

    @Test
    public void testWithdrawAccountNotFound() {
        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.empty());
        try {
            accountService.withdraw(defaultTransactionDTO());
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("The withdrawal wasn't successful", message);
        }
    }

    @Test
    public void testWithdrawalWithAccountDisable() {
        IcesiAccount account = defaultIcesiAccount();
        account.setActive(false);

        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.of(account));
        try {
            accountService.withdraw(defaultTransactionDTO());
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("The withdrawal wasn't successful", message);
        }
    }

    @Test
    public void testWithdrawalWithoutFunds() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(0L);

        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.of(account));
        try {
            accountService.withdraw(defaultTransactionDTO());
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("The account with number 000-000000-00 doesn't have sufficient funds", message);
        }
    }

    @Test
    public void testDeposit() {
        IcesiAccount icesiAccount = defaultIcesiAccount();

        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.ofNullable(icesiAccount));

        ResponseTransactionDTO deposit = accountService.deposit(defaultTransactionDTO());

        assertEquals("The deposit was successfully carried out", deposit.getResult());
        assertEquals(150L, getBalance(icesiAccount));
    }

    @Test
    public void testDepositAccountNotFound() {
        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.empty());

        try{
            accountService.deposit(defaultTransactionDTO());
        }catch (IcesiException exception){
            String message = exception.getMessage();
            assertEquals("The deposit wasn't successful", message);
        }
    }

    @Test
    public void testDepositAccountDisabled() {
        IcesiAccount account = defaultIcesiAccount();
        account.setActive(false);
        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.of(account));

        try{
            accountService.deposit(defaultTransactionDTO());
        }catch (IcesiException exception){
            String message = exception.getMessage();
            assertEquals("The deposit wasn't successful", message);
        }
    }

    @Test
    public void testTransferMoney() {
        IcesiAccount accountFrom = defaultIcesiAccount();
        accountFrom.setType(AccountType.NORMAL.getType());
        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("000-000000-01")
                .active(true)
                .balance(100L)
                .type(AccountType.NORMAL.getType())
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findAccountByAccountNumber("000-000000-01", true)).thenReturn(Optional.of(accountTo));

        ResponseTransactionDTO transaction = accountService.transfer(defaultTransactionDTO());

        verify(accountRepository, times(1)).findAccountByAccountNumber("000-000000-00", true);
        verify(accountRepository, times(1)).findAccountByAccountNumber("000-000000-01", true);
        verify(accountRepository, times(2)).save(any());
        verify(accountMapper, times(1)).fromTransactionDTO(any(), any());
        assertEquals(50L, accountFrom.getBalance());
        assertEquals(150L, accountTo.getBalance());
        assertEquals("The transaction was successfully completed", transaction.getResult());
    }

    @Test
    public void testAccountDepositOnlyCantTransferMoney() {
        IcesiAccount accountFrom = defaultIcesiAccount();
        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("000-000000-01")
                .active(true)
                .balance(100L)
                .type(AccountType.NORMAL.getType())
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findAccountByAccountNumber("000-000000-01", true)).thenReturn(Optional.of(accountTo));

        try {
            accountService.transfer(defaultTransactionDTO());
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("The account with number 000-000000-00 can't transfer money", message);
        }
    }

    @Test
    public void testAccountDepositOnlyCantBeTransferredMoney() {
        IcesiAccount accountFrom = defaultIcesiAccount();
        accountFrom.setType(AccountType.NORMAL.getType());
        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("000-000000-01")
                .active(true)
                .balance(100L)
                .type(AccountType.DEPOSIT_ONLY.getType())
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findAccountByAccountNumber("000-000000-01", true)).thenReturn(Optional.of(accountTo));

        try {
            accountService.transfer(defaultTransactionDTO());
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("The account with number 000-000000-01 can't be transferred money", message);
        }
    }

    @Test
    public void testTransferWithoutFunds() {
        IcesiAccount accountFrom = defaultIcesiAccount();
        accountFrom.setType(AccountType.NORMAL.getType());
        accountFrom.setBalance(0L);
        IcesiAccount accountTo = IcesiAccount.builder()
                .accountNumber("000-000000-01")
                .active(true)
                .balance(100L)
                .type(AccountType.NORMAL.getType())
                .user(defaultIcesiUser())
                .build();

        when(accountRepository.findAccountByAccountNumber("000-000000-00", true)).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findAccountByAccountNumber("000-000000-01", true)).thenReturn(Optional.of(accountTo));

        try {
            accountService.transfer(defaultTransactionDTO());
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("The account with number 000-000000-00 doesn't have sufficient funds", message);
        }
    }

    public long getBalance(IcesiAccount icesiAccountToFind) {
        Optional<IcesiAccount> icesiAccount = Optional.ofNullable(icesiAccountToFind);
        Optional<Long> balance = icesiAccount.map(IcesiAccount::getBalance);
        return balance.orElseGet(() -> 0L);
    }

    public String getAccountNumber(IcesiAccount icesiAccountToFind) {
        Optional<IcesiAccount> icesiAccount = Optional.ofNullable(icesiAccountToFind);
        Optional<String> accountNumber = icesiAccount.map(IcesiAccount::getAccountNumber);
        return accountNumber.orElseGet(() -> "");
    }


    private RequestTransactionDTO defaultTransactionDTO() {
        return RequestTransactionDTO.builder()
                .amount(50L)
                .accountFrom("000-000000-00")
                .accountTo("000-000000-01")
                .build();
    }

    private IcesiAccount defaultIcesiAccount() {
        return IcesiAccount.builder()
                .accountNumber("000-000000-00")
                .balance(100L)
                .type(depositOnly.getType())
                .user(defaultIcesiUser())
                .active(true)
                .build();
    }

    private RequestAccountDTO defaultAccountDTO() {
        return RequestAccountDTO.builder()
                .accountNumber("000-000000-00")
                .balance(100L)
                .type(depositOnly.getType())
                .active(true)
                .icesiUserId(UUID.fromString("e9cd0f1a-29a2-4eb0-9271-3e6a36fa40dc"))
                .build();
    }

    private IcesiUser defaultIcesiUser() {
        return IcesiUser.builder()
                .userId(UUID.fromString("e9cd0f1a-29a2-4eb0-9271-3e6a36fa40dc"))
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .phoneNumber("1234567890")
                .password("password")
                .role(defaultIcesiRole())
                .build();
    }

    private IcesiRole defaultIcesiRole() {
        return IcesiRole.builder()
                .name("Student")
                .description("Loreno Insomnio, nunca supe como se dice")
                .build();
    }
}
