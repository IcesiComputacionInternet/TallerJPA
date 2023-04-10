package co.com.icesi.TallerJPA.unit.service.service;

import co.com.icesi.TallerJPA.dto.IcesiAccountCreateDTO;
import co.com.icesi.TallerJPA.dto.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.dto.IcesiTransactionDTO;
import co.com.icesi.TallerJPA.dto.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.enums.AccountType;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapperImpl;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiAccountRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import co.com.icesi.TallerJPA.service.IcesiAccountService;
import co.com.icesi.TallerJPA.unit.service.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiAccountServiceTest {
    private IcesiAccountService accountService;
    private IcesiAccountRepository accountRepository;
    private IcesiAccountMapper accountMapper;
    private IcesiUserRepository userRepository;

    @BeforeEach
    public void setup() {
        accountRepository = mock(IcesiAccountRepository.class);
        accountMapper = spy(IcesiAccountMapperImpl.class);
        userRepository = mock(IcesiUserRepository.class);
        accountService = new IcesiAccountService(accountRepository,userRepository,accountMapper);
    }

    @Test
    public void testCreateAccount() {
        IcesiUser user=defaultIcesiUser();
        IcesiAccountCreateDTO accountDTO=defaultIcesiAccountDTO();
        IcesiAccount account=defaultIcesiAccount();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        accountService.save(accountDTO);

        verify(userRepository, times(1)).findByEmail(any());
        verify(accountMapper, times(1)).fromIcesiAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new IcesiAccountMatcher(account)));
    }

    @Test
    public void testCreateAccountWithANonExistentUser(){
        IcesiAccountCreateDTO accountDTO=defaultIcesiAccountDTO();
        IcesiAccount account=defaultIcesiAccount();

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        try {
            accountService.save(accountDTO);
            fail();
        }catch (Exception e){
            assertEquals("The user "+accountDTO.getIcesiUser()+" doesn't exist in the database",e.getMessage());
        }
    }

    @Test
    public void testDisableAccount(){
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(0L);
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.of(account));
        accountService.disableAccount(account.getAccountNumber());
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(account)));
        assertEquals("The account was disabled successfully", accountService.disableAccount(account.getAccountNumber()));
    }

    @Test
    public void testDisableANotExistAccount(){
        IcesiAccount account = defaultIcesiAccount();
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.empty());
        try {
            accountService.disableAccount(account.getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("Account could not be found for deactivation",e.getMessage());
        }
    }

    @Test
    public void testDisableADisableAccount(){
        IcesiAccount account = defaultIcesiAccount();
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.of(account));
        try {
            accountService.disableAccount(account.getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("This account cannot be disabled because it is already disabled",e.getMessage());
        }
    }

    @Test
    public void testEnableAccount(){
        IcesiAccount account = defaultIcesiAccount();
        account.setActive(false);
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.of(account));
        accountService.enableAccount(account.getAccountNumber());
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(account)));
        assertEquals("The account was enabled successfully", accountService.enableAccount(account.getAccountNumber()));
    }

    @Test
    public void testEnableANotExistAccount(){
        IcesiAccount account = defaultIcesiAccount();
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.empty());
        try {
            accountService.enableAccount(account.getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("Account could not be found for activation",e.getMessage());
        }
    }

    @Test
    public void testEnableAnEnabledAccount(){
        IcesiAccount account = defaultIcesiAccount();
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.of(account));
        try {
            accountService.enableAccount(account.getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("This account cannot be enabled because is already active",e.getMessage());
        }
    }

    @Test
    public void testGetIcesiAccountNumber() {
        String accountNumber = "799-948879-27";
        IcesiAccount account = defaultIcesiAccount();

        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        IcesiAccount returnedAccount = accountService.getIcesiAccountNumber(accountNumber);

        assertEquals(accountNumber, returnedAccount.getAccountNumber());
        verify(accountRepository, times(1)).findAccountByAccountNumber(accountNumber);
    }

    @Test
    public void testGetIcesiAccountNumberNotFound() {
        String accountNumber = "889-123456-03";
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        try {
            accountService.getIcesiAccountNumber(accountNumber);
            fail();
        } catch (Exception e) {
            assertEquals("This account "+accountNumber+" doesn't exist in the database",e.getMessage());
        }
    }

    @Test
    public void testAccountNumberFormat(){
        IcesiUser user=defaultIcesiUser();
        IcesiAccountCreateDTO accountDTO=defaultIcesiAccountDTO();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        accountService.save(accountDTO);
        verify(accountRepository, times(1)).save(argThat(this::isValidAccountNumberFormat));
    }

    @Test
    public void testDepositMoney() {
        IcesiAccount icesiAccount = defaultIcesiAccount();
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        IcesiTransactionDTO transactionDTO= IcesiTransactionDTO.builder()
                .accountNumberDestination("799-948879-27")
                .amount(10L)
                .build();
        accountService.depositOnly(transactionDTO);
        verify(accountRepository, times(1)).findAccountByAccountNumber(any());
        assertEquals(110L, icesiAccount.getBalance());
        assertEquals("The deposit was made successfully", transactionDTO.getMessageResult());
    }

    @Test
    public void testDepositMoneyInANotExistAccount() {
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.empty());
        IcesiTransactionDTO transactionDTO= IcesiTransactionDTO.builder()
                .accountNumberDestination("799-948879-27")
                .amount(4L)
                .build();
        try {
            accountService.depositOnly(transactionDTO);
            fail();
        } catch (Exception e) {
            assertEquals("The account "+transactionDTO.getAccountNumberOrigin()+" does not exist, deposit cannot be made",e.getMessage());
        }
    }

    @Test
    public void testDepositMoneyWithAnDisableAccount() {
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        IcesiTransactionDTO transactionDTO= IcesiTransactionDTO.builder()
                .accountNumberDestination("799-948879-27")
                .amount(4L)
                .build();
        try {
            accountService.depositOnly(transactionDTO);
            fail();
        } catch (Exception e) {
            assertEquals("This account is not active, deposit cannot be made",e.getMessage());
        }
    }

    @Test
    public void testwithdrawalMoney() {
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setAccountType(AccountType.STANDARD_ACCOUNT);
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        IcesiTransactionDTO transactionDTO= IcesiTransactionDTO.builder()
                .accountNumberOrigin("799-948879-27")
                .amount(10L)
                .build();
        accountService.withdrawal(transactionDTO);
        verify(accountRepository, times(1)).findAccountByAccountNumber(any());
        assertEquals(90L, icesiAccount.getBalance());
        assertEquals("The Withdrawal was made successfully", transactionDTO.getMessageResult());
    }

    @Test
    public void testwithdrawalMoneyInANotExistAccount() {
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.empty());
        IcesiTransactionDTO transactionDTO= IcesiTransactionDTO.builder()
                .accountNumberDestination("799-948879-27")
                .amount(4L)
                .build();
        try {
            accountService.withdrawal(transactionDTO);
            fail();
        } catch (Exception e) {
            assertEquals("The account "+transactionDTO.getAccountNumberDestination()+" does not exist, withdrawal cannot be made",e.getMessage());
        }
    }

    @Test
    public void testwithdrawalMoneyWithAnDisableAccount() {
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setAccountType(AccountType.STANDARD_ACCOUNT);
        icesiAccount.setActive(false);
        when(accountRepository.findAccountByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        IcesiTransactionDTO transactionDTO= IcesiTransactionDTO.builder()
                .accountNumberDestination("799-948879-27")
                .amount(4L)
                .build();
        try {
            accountService.withdrawal(transactionDTO);
            fail();
        } catch (Exception e) {
            assertEquals("This account is not active, withdrawal cannot be made",e.getMessage());
        }
    }

    @Test
    public void testTransferMoney(){
        IcesiAccount accountF = defaultIcesiAccount();
        accountF.setAccountType(AccountType.STANDARD_ACCOUNT);

        IcesiAccount accountT = IcesiAccount.builder()
                .accountNumber("452-976314-32")
                .balance(100L)
                .active(true)
                .accountType(AccountType.STANDARD_ACCOUNT)
                .icesiUser(defaultIcesiUser())
                .build();

        IcesiTransactionDTO transactionDTO = IcesiTransactionDTO.builder()
                .accountNumberOrigin(accountF.getAccountNumber())
                .accountNumberDestination(accountT.getAccountNumber())
                .amount(35L)
                .build();

        when(accountRepository.findAccountByAccountNumber("799-948879-27")).thenReturn(Optional.of(accountF));
        when(accountRepository.findAccountByAccountNumber("452-976314-32")).thenReturn(Optional.of(accountT));

        accountService.transferMoney(transactionDTO);

        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(accountF)));
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(accountT)));

        assertEquals(65L, accountF.getBalance());
        assertEquals(135L, accountT.getBalance());
        assertEquals("The transfer was made successfully", transactionDTO.getMessageResult());
    }

    @Test
    public void testTransferMoneyWithDepositAccount(){
        IcesiAccount accountF = defaultIcesiAccount();
        IcesiAccount accountT = IcesiAccount.builder()
                .accountNumber("452-976314-32")
                .balance(100L)
                .active(true)
                .accountType(AccountType.STANDARD_ACCOUNT)
                .icesiUser(defaultIcesiUser())
                .build();

        IcesiTransactionDTO transactionDTO = IcesiTransactionDTO.builder()
                .accountNumberOrigin(accountF.getAccountNumber())
                .accountNumberDestination(accountT.getAccountNumber())
                .amount(35L)
                .build();

        when(accountRepository.findAccountByAccountNumber("799-948879-27")).thenReturn(Optional.of(accountF));
        when(accountRepository.findAccountByAccountNumber("452-976314-32")).thenReturn(Optional.of(accountT));

        try {
            accountService.transferMoney(transactionDTO);
        } catch (Exception e) {
            assertEquals("This account "+accountF.getAccountNumber()+" can't transfer because is marked as 'deposit only'", e.getMessage());
        }
    }

    @Test
    public void testTransferMoneyWith0Amount(){
        IcesiAccount accountF = defaultIcesiAccount();
        accountF.setAccountType(AccountType.STANDARD_ACCOUNT);
        accountF.setBalance(0L);
        IcesiAccount accountT = IcesiAccount.builder()
                .accountNumber("452-976314-32")
                .balance(100L)
                .active(true)
                .accountType(AccountType.STANDARD_ACCOUNT)
                .icesiUser(defaultIcesiUser())
                .build();

        IcesiTransactionDTO transactionDTO = IcesiTransactionDTO.builder()
                .accountNumberOrigin(accountF.getAccountNumber())
                .accountNumberDestination(accountT.getAccountNumber())
                .amount(35L)
                .build();

        when(accountRepository.findAccountByAccountNumber("799-948879-27")).thenReturn(Optional.of(accountF));
        when(accountRepository.findAccountByAccountNumber("452-976314-32")).thenReturn(Optional.of(accountT));

        try {
            accountService.transferMoney(transactionDTO);
        } catch (Exception e) {
            assertEquals("Not enough money to transfer to the account", e.getMessage());
        }
    }

    @Test
    public void testTransferMoneyWithOneOfTheNonExistAccount(){
        IcesiAccount accountF = defaultIcesiAccount();
        accountF.setAccountType(AccountType.STANDARD_ACCOUNT);

        IcesiTransactionDTO transactionDTO = IcesiTransactionDTO.builder()
                .accountNumberOrigin(accountF.getAccountNumber())
                .accountNumberDestination("000-000000-00")
                .amount(35L)
                .build();

        when(accountRepository.findAccountByAccountNumber("799-948879-27")).thenReturn(Optional.of(accountF));
        when(accountRepository.findAccountByAccountNumber("000-000000-00")).thenReturn(Optional.empty());

        try {
            accountService.transferMoney(transactionDTO);
        } catch (Exception e) {
            assertEquals("This account "+"000-000000-00"+" doesn't exist in the database", e.getMessage());
        }
    }

    private boolean isValidAccountNumberFormat(IcesiAccount account) {
        return account.getAccountNumber().matches("[0-9]{3}-[0-9]{6}-[0-9]{2}");
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("799-948879-27")
                .balance(100L)
                .active(true)
                .accountType(AccountType.DEPOSIT_ONLY)
                .icesiUser(defaultIcesiUser())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("db170d86-c7cd-4f46-a4a0-bbe58934a2b0"))
                .firstName("Michael")
                .lastName("Jackson")
                .email("jekag85543@marikuza.com")
                .phoneNumber("228322647761")
                .password("pruebapassword")
                .role(defaultRole())
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("Administrador")
                .description("El administrador se encarga de administrar la página")
                .build();
    }

    private IcesiAccountCreateDTO defaultIcesiAccountDTO(){
        return IcesiAccountCreateDTO.builder()
                .balance(100L)
                .accountType(AccountType.DEPOSIT_ONLY)
                .icesiUser(defaultUserDTO())
                .build();
    }

    private IcesiUserCreateDTO defaultUserDTO(){
        return IcesiUserCreateDTO.builder()
                .firstName("Michael")
                .lastName("Jackson")
                .email("jekag85543@marikuza.com")
                .phoneNumber("228322647761")
                .password("pruebapassword")
                .role(defaultRoleDTO())
                .build();
    }

    private IcesiRoleCreateDTO defaultRoleDTO(){
        return   IcesiRoleCreateDTO.builder()
                .name("Administrador")
                .description("El administrador se encarga de administrar la página")
                .build();
    }
}
