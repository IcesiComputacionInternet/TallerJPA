package co.com.icesi.TallerJPA.unit.service;

import co.com.icesi.TallerJPA.Enum.AccountType;
import co.com.icesi.TallerJPA.dto.AccountCreateDTO;
import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.dto.TransactionOperationDTO;
import co.com.icesi.TallerJPA.dto.response.UserResponseDTO;
import co.com.icesi.TallerJPA.error.exception.ArgumentsException;
import co.com.icesi.TallerJPA.mapper.AccountMapper;
import co.com.icesi.TallerJPA.mapper.AccountMapperImpl;
import co.com.icesi.TallerJPA.mapper.responseMapper.AccountResponseMapper;
import co.com.icesi.TallerJPA.mapper.responseMapper.AccountResponseMapperImpl;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.AccountRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import co.com.icesi.TallerJPA.security.IcesiSecurityContext;
import co.com.icesi.TallerJPA.service.AccountService;
import co.com.icesi.TallerJPA.unit.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private AccountService accountService;
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountMapper accountMapper;

    private AccountResponseMapper accountResponseMapper;

    @BeforeEach
    private void init(){
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        accountResponseMapper = spy(AccountResponseMapperImpl.class);
        IcesiSecurityContext securityContext = mock(IcesiSecurityContext.class);
        accountService = new AccountService(accountRepository,userRepository,accountMapper,accountResponseMapper,securityContext);
        when(securityContext.getCurrentUserRole()).thenReturn("ADMIN");
    }


    @Test
    public void testCreateAccount(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountCreateDTO());
        verify(userRepository, times(1)).findUserByEmail(any());
        verify(accountMapper,times(1)).fromIcesiAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new IcesiAccountMatcher(defaultIcesiAccount())));
    }

    @Test
    public void testCreateAccountWithUserNotFound(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            accountService.save(defaultAccountCreateDTO());
        }catch (RuntimeException e) {
            assertEquals("User not found",e.getMessage());
        }
        verify(userRepository, times(1)).findUserByEmail(any());
        verify(accountMapper,times(1)).fromIcesiAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new IcesiAccountMatcher(defaultIcesiAccount())));

    }

    @Test
    public void testCreateAccountWithNegativeBalance(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            accountService.save(defaultAccountCreateDTO());
        }catch (RuntimeException e) {
            assertEquals("Balance can't be negative",e.getMessage());
        }
        verify(userRepository, times(1)).findUserByEmail(any());
        verify(accountMapper,times(1)).fromIcesiAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new IcesiAccountMatcher(defaultIcesiAccount())));

    }

    @Test
    public void testGetAccountByAccountNumber(){
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount account = accountService.getAccountByAccountNumber("12345");
        assertEquals(defaultIcesiAccount().getAccountNumber(),account.getAccountNumber());
        verify(accountRepository, times(1)).findAccount(any());
    }

    @Test
    public void testGetAccountByAccountNumberWithAccountNotFound(){
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        try{
            accountService.getAccountByAccountNumber("12345");
        }catch (RuntimeException e) {
            assertEquals("Account not found",e.getMessage());
        }
        verify(accountRepository, times(1)).findAccount(any());

    }


    @Test
    public void testGenerateAccountNumber(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountCreateDTO());
        verify(accountRepository,times(1)).findByAccountNumber(any());
    }

    @Test
    public void testGenerateAccountNumberAlreadyExists(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.findByAccountNumber(any())).thenReturn(true,false);
        accountService.save(defaultAccountCreateDTO());
        verify(accountRepository,times(2)).findByAccountNumber(any());
    }

    @Test
    public void testEnableAccount(){
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount account = accountService.getAccountByAccountNumber("12345");
        account.setActive(false);
        String msg = accountService.enableAccount(account.getAccountNumber());
        assertEquals("The account is enabled",msg);
        verify(accountRepository,times(1)).updateState(account.getAccountNumber(),true);
    }

    @Test
    public void testEnableAccountNotFound(){
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        try{
            accountService.enableAccount("12345");
        }catch (RuntimeException e) {
            assertEquals("Account not found",e.getMessage());
            verify(accountRepository,times(1)).updateState("12345",true);
        }


    }

    @Test
    public void testDisableAccount(){
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount account = accountService.getAccountByAccountNumber("12345");
        account.setActive(true);
        String msg = accountService.disableAccount(account.getAccountNumber());
        assertEquals("The account was disabled",msg);
        verify(accountRepository,times(1)).updateState(account.getAccountNumber(),false);

    }

    @Test
    public void testDisableAccountNotFound(){
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        try{
            accountService.disableAccount("12345");
        }catch (RuntimeException e) {
            assertEquals("Account not found",e.getMessage());
            verify(accountRepository,times(1)).updateState(defaultIcesiAccount().getAccountNumber(), true);
        }
    }

    @Test
    public void testWithdrawToAccountDeposit_Only(){
        IcesiAccount account = defaultIcesiAccount();
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(account));
        TransactionOperationDTO accountFrom = TransactionOperationDTO.builder()
                .accountFrom(account.getAccountNumber())
                .amount(10L)
                .build();

        try{
            accountService.withdraw(accountFrom);
        }catch (RuntimeException e) {
            assertEquals("The account "+ accountFrom.getAccountFrom()+" is deposit only",e.getMessage());
        }

    }

    @Test
    public void testWithdrawWithAccountNotFound(){
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(defaultIcesiAccount1()));
        try{
            accountService.withdraw(defaultTransactionOperationDTO2());
        }catch (RuntimeException e) {
            assertEquals("Account not found",e.getMessage());
            verify(accountRepository,times(1)).updateAccount(defaultIcesiAccount().getAccountNumber(),defaultIcesiAccount().getBalance());
        }
    }




    @Test
    public void testCreateAccountWhenUserDoesNotExist(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());
        try{
            accountService.save(defaultAccountCreateDTO());
        }catch (Exception e) {
            String message = e.getMessage();
            assertEquals("User not found",message);
        }
    }

    @Test
    public void testFindAccountByAccountNumber(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccount));
        IcesiAccount account = accountService.getAccountByAccountNumber(icesiAccount.getAccountNumber());
        assertEquals(icesiAccount.getAccountNumber(),account.getAccountNumber());
        verify(accountRepository, times(1)).findAccount(any());
    }

    /*

    @Test
    public void testFindAccountByAccountNumberWhenAccountDoesNotExist(){
        when(accountRepository.findAccount(any())).thenReturn(Optional.empty());
        try{
            accountService.getAccountByAccountNumber(accountService.());
        }catch (Exception e) {
            String message = e.getMessage();
            assertEquals("Account not found",message);
        }
    }

     */

    @Test
    public void testEnableAccountWhenBalance0(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(0L);
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccount));
        String message = accountService.enableAccount(icesiAccount.getAccountNumber());
        verify(accountRepository, times(1)).findAccount(any());
        assertEquals("The account is enabled",message);
    }

    @Test
    public void testEnableAccountWhenBalanceNot0(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(1000L);

        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccount));
        String message = accountService.enableAccount(icesiAccount.getAccountNumber());
        verify(accountRepository, times(1)).findAccount(any());
        assertEquals("The account is enabled",message);
    }

    @Test
    public void testWitdrawWhenBalanceIsEnough(){
        IcesiAccount icesiAccount = defaultIcesiAccount1();
        icesiAccount.setBalance(2000L);
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccount));
        TransactionOperationDTO message = accountService.withdraw(defaultTransactionOperationDTO());
        assertEquals("Withdraw successful",message.getResult());
    }

    @Test
    public void testWitdrawWhenBalanceIsNotEnough(){
        IcesiAccount icesiAccount = defaultIcesiAccount1();
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdraw(defaultTransactionOperationDTO());
        }catch (RuntimeException e) {
            String message = e.getMessage();
            assertEquals("Insufficient funds: "+icesiAccount.getBalance(),message);
        }
    }

    @Test
    public void testWithdrawWhenAccountIsInactive(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdraw(defaultTransactionOperationDTO2());
        }catch (RuntimeException e) {
            String message = e.getMessage();
            assertEquals("The account "+icesiAccount.getAccountNumber()+" is disabled",message);
        }
    }

    @Test
    public void testDepositWhenAccountIsActive(){
        IcesiAccount icesiAccount = defaultIcesiAccount1();
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccount));
        TransactionOperationDTO message = accountService.deposit(defaultTransactionOperationDTO());
        assertEquals("Deposit successful",message.getResult());
    }

    @Test
    public void testDepositWhenAccountIsInactive(){
        IcesiAccount icesiAccount = defaultIcesiAccount1();
        icesiAccount.setActive(false);
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.deposit(defaultTransactionOperationDTO());
        }catch (RuntimeException e) {
            String message = e.getMessage();
            assertEquals("The account "+icesiAccount.getAccountNumber()+" is disabled",message);
        }
    }

    @Test
    public void testTransferWhenAccountsAreActive(){
        IcesiAccount icesiAccountOrigin = defaultIcesiAccount2();
        IcesiAccount icesiAccountDestination = defaultIcesiAccount1();

        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccountOrigin));
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccountDestination));
        TransactionOperationDTO message = accountService.transfer(defaultTransactionOperationDTO());
        verify(accountRepository, times(2)).findAccount(any());
        assertEquals("Transfer successful",message.getResult());
    }

    @Test
    public void testTransferWhenAccountIsInactive(){
        IcesiAccount icesiAccountOrigin = defaultIcesiAccount1();
        IcesiAccount icesiAccountDestination = defaultIcesiAccount2();
        icesiAccountOrigin.setActive(false);
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccountOrigin));
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccountDestination));
        try{
            accountService.transfer(defaultTransactionOperationDTO());
        }catch (ArgumentsException e) {
            String message = e.getMessage();
            assertEquals("Account must be active to transfer money",message);
        }
    }

    @Test
    public void testTransferWhenBalanceIsNotEnough(){
        IcesiAccount icesiAccountOrigin = defaultIcesiAccount1();
        IcesiAccount icesiAccountDestination = defaultIcesiAccount2();

        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccountOrigin));
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccountDestination));
        try{
            accountService.transfer(defaultTransactionOperationDTO());
        }catch (RuntimeException e) {
            String message = e.getMessage();
            assertEquals("Insufficient funds: "+icesiAccountOrigin.getBalance(),message);
        }
    }

    @Test
    public void testTransferWhenTypeIsDepositOnly(){
        IcesiAccount icesiAccountOrigin = defaultIcesiAccount1();
        IcesiAccount icesiAccountDestination = defaultIcesiAccount2();

        icesiAccountOrigin.setType(AccountType.DEPOSIT_ONLY);
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccountOrigin));
        when(accountRepository.findAccount(any())).thenReturn(Optional.of(icesiAccountDestination));
        try{
            accountService.transfer(defaultTransactionOperationDTO2());
        }catch (RuntimeException e) {
            String message = e.getMessage();
            assertEquals("The acount "+icesiAccountOrigin.getAccountNumber()+" is deposit only",message);
        }
    }





    private AccountCreateDTO defaultAccountCreateDTO(){
        return AccountCreateDTO.builder()
                .balance(100)
                .type(AccountType.DEPOSIT_ONLY)
                .user("test@hotmail.com")
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("12345")
                .balance(100L)
                .type(AccountType.DEPOSIT_ONLY)
                .active(true)
                .user(defaultIcesiUser())
                .build();
    }

    private IcesiAccount defaultIcesiAccount1(){
        return IcesiAccount.builder()
                .accountNumber("54646547")
                .balance(100L)
                .type(AccountType.AHORROS)
                .active(true)
                .user(defaultIcesiUser())
                .build();
    }
    private IcesiAccount defaultIcesiAccount2(){
        return IcesiAccount.builder()
                .accountNumber("67890")
                .balance(10000L)
                .type(AccountType.AHORROS)
                .active(true)
                .user(defaultIcesiUser())
                .build();
    }

    private IcesiAccount defaultIcesiAccount3(){
        return IcesiAccount.builder()
                .accountNumber("9843687")
                .balance(100L)
                .type(AccountType.AHORROS)
                .active(true)
                .user(defaultIcesiUser())
                .build();
    }

    private TransactionOperationDTO defaultTransactionOperationDTO(){
        return TransactionOperationDTO.builder()
                .accountTo("54646547")
                .accountFrom("67890")
                .amount(1L)
                .build();
    }

    private TransactionOperationDTO defaultTransactionOperationDTO2(){
        return TransactionOperationDTO.builder()
                .accountTo("67890")
                .accountFrom("54646547")
                .amount(10L)
                .build();
    }


    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("1234")
                .password("1234")
                .role(defaultRole())
                .build();
    }
    private RoleCreateDTO defaultRoleCreateDTO() {
        return RoleCreateDTO.builder()
                .name("Admin")
                .description("Ninguna")
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("Admin")
                .description("Admin")
                .build();
    }




}
