package com.edu.icesi.demojpa.unit.service.Test;

import com.edu.icesi.demojpa.Enum.AccountType;
import com.edu.icesi.demojpa.dto.AccountCreateDTO;
import com.edu.icesi.demojpa.dto.UserCreateDTO;
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
    private void init(){
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        accountService = new AccountService(accountRepository, userRepository, accountMapper);
    }

    @Test
    public void testCreateAccount(){
        when(userRepository.findUserById(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.empty());
        IcesiAccount icesiAccount = accountService.save(defaultAccountDTO());
        IcesiAccount icesiAccountToCompare = defaultIcesiAccountToCompareWithDTO();
        verify(accountRepository, times(1)).save(argThat(new IcesiAccountMatcher(icesiAccountToCompare)));
    }

    @Test
    public void testAccountDepositOnlyCantTransferMoney(){
        when(accountRepository.findAccountByAccountNumber("000-000000-00")).thenReturn(Optional.ofNullable(defaultIcesiAccount()));
        when(accountRepository.findAccountByAccountNumber("000-000000-01")).thenReturn(Optional.ofNullable(depositOnlyIcesiAccount()));
        try{
            accountService.transferMoneyToAnotherAccount(depositOnlyIcesiAccount().getAccountNumber(), defaultIcesiAccount().getAccountNumber(), 50);
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The account with number account 000-000000-01 can't transfer or be transferred money", message);
        }
    }

    @Test
    public void testAccountDepositOnlyCantBeTransferredMoney(){
        when(accountRepository.findAccountByAccountNumber("000-000000-00")).thenReturn(Optional.ofNullable(defaultIcesiAccount()));
        when(accountRepository.findAccountByAccountNumber("000-000000-01")).thenReturn(Optional.ofNullable(depositOnlyIcesiAccount()));
        try{
            accountService.transferMoneyToAnotherAccount(defaultIcesiAccount().getAccountNumber(), depositOnlyIcesiAccount().getAccountNumber(), 50);
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The account with number account 000-000000-01 can't transfer or be transferred money", message);
        }
    }

    @Test
    public void testDisableAccountWithBalance(){
        when(accountRepository.findAccountByAccountNumber("000-000000-00")).thenReturn(Optional.ofNullable(defaultIcesiAccount()));
        String message = accountService.disableAccount("000-000000-00");
        assertEquals("The account couldn't be deactivated because it is funded", message);
    }

    @Test
    public void testDisableAccountWithoutBalance(){
        when(accountRepository.findAccountByAccountNumber("000-000000-02")).thenReturn(Optional.ofNullable(noFundsIcesiAccount()));
        String message = accountService.disableAccount("000-000000-02");
        assertEquals("The account has been deactivated", message);
    }

    @Test
    public void testEnableAccount(){
        when(accountRepository.findAccountByAccountNumber("000-000000-01")).thenReturn(Optional.ofNullable(depositOnlyIcesiAccount()));
        String message = accountService.enableAccount("000-000000-01");
        assertEquals("The account has been activated", message);
    }

    @Test
    public void testWithdrawalWithFunds(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        when(accountRepository.findAccountByAccountNumber("000-000000-00")).thenReturn(Optional.ofNullable(icesiAccount));
        String message = accountService.withdrawal("000-000000-00", 50);
        long balance = getBalance(icesiAccount);
        assertEquals(50L, balance);
        assertEquals("The withdrawal was successfully carried out", message);
    }

    @Test
    public void testWithdrawalWithoutFunds(){
        when(accountRepository.findAccountByAccountNumber("000-000000-02")).thenReturn(Optional.ofNullable(noFundsIcesiAccount()));
        try{
            accountService.withdrawal("000-000000-02", 50);
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The account with account number 000-000000-02 doesn't have sufficient funds", message);
        }
    }

    @Test
    public void testWithdrawalWithAccountDisable(){
        when(accountRepository.findAccountByAccountNumber("000-000000-03")).thenReturn(Optional.ofNullable(disableIcesiAccount()));
        try{
            accountService.withdrawal("000-000000-03", 50);
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The account with account number 000-000000-03 isn't enabled", message);
        }
    }

    @Test
    public void testDepositMoney(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        when(accountRepository.findAccountByAccountNumber("000-000000-00")).thenReturn(Optional.ofNullable(icesiAccount));
        String message = accountService.depositMoney("000-000000-00", 50);
        long balance = getBalance(icesiAccount);
        assertEquals("The deposit was successfully carried out", message);
        assertEquals(150L, balance);
    }

    @Test
    public void testTransferMoney(){
        IcesiAccount icesiAccountToWithdrawal = defaultIcesiAccount();
        IcesiAccount icesiAccountToDeposit = normalIcesiAccount();
        when(accountRepository.findAccountByAccountNumber("000-000000-00")).thenReturn(Optional.ofNullable(icesiAccountToWithdrawal));
        when(accountRepository.findAccountByAccountNumber("000-000000-05")).thenReturn(Optional.ofNullable(icesiAccountToDeposit));
        String message = accountService.transferMoneyToAnotherAccount(getAccountNumber(icesiAccountToWithdrawal), getAccountNumber(icesiAccountToDeposit), 50);
        long balanceDeposited = getBalance(icesiAccountToDeposit);
        long balanceWithdrawal = getBalance(icesiAccountToWithdrawal);

        assertEquals("The transaction was successfully completed", message);
        assertEquals(150, balanceDeposited);
        assertEquals(50, balanceWithdrawal);
    }

    public long getBalance(IcesiAccount icesiAccountToFind){
        Optional<IcesiAccount> icesiAccount = Optional.ofNullable(icesiAccountToFind);
        Optional<Long> balance = icesiAccount.map(IcesiAccount::getBalance);
        return balance.orElseGet(() -> 0L);
    }

    public String getAccountNumber(IcesiAccount icesiAccountToFind){
        Optional<IcesiAccount> icesiAccount = Optional.ofNullable(icesiAccountToFind);
        Optional<String> accountNumber = icesiAccount.map(IcesiAccount::getAccountNumber);
        return accountNumber.orElseGet(() -> "");
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("000-000000-00")
                .balance(100)
                .type(normalAccount.getType())
                .user(defaultIcesiUser())
                .active(true)
                .build();
    }

    private IcesiAccount normalIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("000-000000-05")
                .balance(100)
                .type(normalAccount.getType())
                .user(defaultIcesiUser())
                .active(true)
                .build();
    }

    private IcesiAccount depositOnlyIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("000-000000-01")
                .balance(100)
                .type(depositOnly.getType())
                .user(defaultIcesiUser())
                .active(true)
                .build();
    }

    private IcesiAccount noFundsIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("000-000000-02")
                .balance(0)
                .type(depositOnly.getType())
                .user(defaultIcesiUser())
                .active(true)
                .build();
    }

    private IcesiAccount disableIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("000-000000-03")
                .balance(0)
                .type(depositOnly.getType())
                .user(defaultIcesiUser())
                .active(false)
                .build();
    }

    private IcesiAccount defaultIcesiAccountToCompareWithDTO(){
        return IcesiAccount.builder()
                .balance(100)
                .type(depositOnly.getType())
                .active(true)
                .user(defaultIcesiUser())
                .build();
    }

    private AccountCreateDTO defaultAccountDTO(){
        return AccountCreateDTO.builder()
                .balance(100)
                .type(depositOnly.getType())
                .active(true)
                .icesiUserId(defaultIcesiUser().getUserId().toString())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
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

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("Student")
                .description("Loreno Insomnio, nunca supe como se dice")
                .build();
    }
}
