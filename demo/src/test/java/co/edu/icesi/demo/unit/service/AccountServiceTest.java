package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.dto.RoleCreateDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import co.edu.icesi.demo.dto.UserCreateDTO;
import co.edu.icesi.demo.mapper.AccountMapper;
import co.edu.icesi.demo.mapper.AccountMapperImpl;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.AccountRepository;
import co.edu.icesi.demo.repository.UserRepository;
import co.edu.icesi.demo.service.AccountService;
import co.edu.icesi.demo.unit.service.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;

    private AccountRepository accountRepository;

    private AccountMapper accountMapper;

    private UserRepository userRepository;

    @BeforeEach
    private void init(){
        accountRepository=mock(AccountRepository.class);
        accountMapper=spy(AccountMapperImpl.class);
        userRepository=mock(UserRepository.class);
        accountService=new AccountService(accountRepository,accountMapper,userRepository);

    }

    @Test
    public void testCreateAccount(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));

        accountService.save(newAccountCreateDTO());
        IcesiAccount icesiAccount= newIcesiAccount();

        verify(accountMapper,times(1)).fromIcesiAccountDTO(any());
        verify(accountRepository,times(1)).findByAccountNumber(any());
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
        verify(accountMapper,times(1)).fromIcesiAccount(any());
    }

    @Test
    public void testAccountNumberFormat(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(newAccountCreateDTO());
        verify(accountRepository,times(1)).save(argThat(a-> a.getAccountNumber().matches("[0-9]{3}-[0-9]{6}-[0-9]{2}")));

    }

    @Test
    public void testCreateAccountWhenUserDoesNotExist(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        try {
            accountService.save(newAccountCreateDTO());
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User does not exists",message);

            verify(accountMapper,never()).fromIcesiAccountDTO(any());
            verify(accountRepository,never()).findByAccountNumber(any());
            verify(accountRepository,never()).save(any());
            verify(accountMapper,never()).fromIcesiAccount(any());
        }
    }

    @Test
    public void testCreateAccountUniqueAccountNumber(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(accountDisabled()),Optional.empty());

        accountService.save(newAccountCreateDTO());

        verify(accountMapper,times(1)).fromIcesiAccountDTO(any());
        verify(accountRepository,times(2)).findByAccountNumber(any());
        verify(accountRepository,times(1)).save(any());
        verify(accountMapper,times(1)).fromIcesiAccount(any());
    }

    @Test
    public void testEnableAccount() {

        when(accountRepository.findByAccountNumber(any(),eq(false))).thenReturn(Optional.of(accountDisabled()));

        accountService.enableAccount(accountDisabled().getAccountNumber());

        verify(accountRepository,times(1)).findByAccountNumber(accountDisabled().getAccountNumber(),false);
        verify(accountRepository,times(1)).save(any());
        verify(accountMapper,times(1)).fromIcesiAccount(argThat(IcesiAccount::isActive));

    }

    @Test
    public void testEnableAccountWhenAccountNotFound() {

        when(accountRepository.findByAccountNumber(any(),eq(false))).thenReturn(Optional.empty());

        try {
            accountService.enableAccount("111-222222-33");
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Inactive account not found",message);

            verify(accountRepository,times(1)).findByAccountNumber(any(),eq(false));
            verify(accountRepository,never()).save(any());
            verify(accountMapper,never()).fromIcesiAccount(any());

        }

    }

    @Test
    public void testDisableAccount() {

        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.of(accountEnabled()));

        accountService.disableAccount(accountEnabled().getAccountNumber());

        verify(accountRepository,times(1)).findByAccountNumber(accountEnabled().getAccountNumber(),true);
        verify(accountRepository,times(1)).save(any());
        verify(accountMapper,times(1)).fromIcesiAccount(argThat(a->!a.isActive()));

    }

    @Test
    public void testDisableAccountWhenAccountNotFound() {

        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.empty());

        try {
            accountService.disableAccount("111-222222-33");
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Active account not found",message);

            verify(accountRepository,times(1)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
            verify(accountMapper,never()).fromIcesiAccount(any());

        }

    }

    @Test
    public void testDisableAccountWhenBalanceIsNotZero() {
        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.of(accountNormalWithBalanceNotInZero()));

        try {
            accountService.disableAccount(accountNormalWithBalanceNotInZero().getAccountNumber());
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Balance is not 0. Account can't be disabled",message);

            verify(accountRepository,times(1)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
            verify(accountMapper,never()).fromIcesiAccount(any());

        }
    }

    @Test
    public void testWithdrawalMoney() {
        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.of(accountNormalWithBalanceNotInZero()));

       TransactionDTO transactionDTO= accountService.withdrawalMoney(defaultTransactionDTO());
       verify(accountRepository,times(1)).findByAccountNumber(any(),eq(true));
       verify(accountRepository,times(1)).save(argThat(a->a.getBalance()==490000));
       assertEquals("Withdrawal successfully completed",transactionDTO.getResult());

    }
/*
    @Test
    public void testWithdrawalMoneyWhenAmountGreaterThanBalance() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(defaultIcesiAccount());
        IcesiAccount icesiAccount = accountService.save(defaultAccountCreateDTO());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try {
            accountService.withdrawalMoney(icesiAccount.getAccountNumber(), 600000);
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Not enough money to withdrawal in the account",message);

        }

    }

    @Test
    public void testWithdrawalMoneyWhenAccountIsDisable() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountDisabled());
        IcesiAccount icesiAccount = accountService.save(accountCreateDTODisabled());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try {
            accountService.withdrawalMoney(icesiAccount.getAccountNumber(), 600000);
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Transaction can't be made, account 123-123456-12 is disabled",message);

        }

    }

    @Test
    public void testDepositMoney() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(defaultIcesiAccount());
        IcesiAccount icesiAccount = accountService.save(defaultAccountCreateDTO());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.depositMoney(icesiAccount.getAccountNumber(), 100000);

        assertEquals(600000, icesiAccount.getBalance());

    }

    @Test
    public void testDepositMoneyWhenAccountIsDisable() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountDisabled());
        IcesiAccount icesiAccount = accountService.save(accountCreateDTODisabled());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try {
            accountService.depositMoney(icesiAccount.getAccountNumber(), 600000);
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Transaction can't be made, account 123-123456-12 is disabled",message);

        }

    }

    @Test
    public void testTransferMoney() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(defaultIcesiAccount());
        IcesiAccount icesiAccountFrom = accountService.save(defaultAccountCreateDTO());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountToDisable());

        IcesiAccount icesiAccountTo = accountService.save(accountCreateDTOToDisable());
        when(accountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(icesiAccountFrom));
        when(accountRepository.findByAccountNumber("123-123456-21")).thenReturn(Optional.of(icesiAccountTo));

        accountService.transferMoney(icesiAccountFrom.getAccountNumber(), icesiAccountTo.getAccountNumber(),100000);

        assertEquals(400000, icesiAccountFrom.getBalance());

        assertEquals(100000, icesiAccountTo.getBalance());

    }

    @Test
    public void testTransferMoneyWhenComesFromDepositOnlyAccount() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountDepositOnly());
        IcesiAccount icesiAccountFrom = accountService.save(accountCreateDTODepositOnly());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountToDisable());
        IcesiAccount icesiAccountTo = accountService.save(accountCreateDTOToDisable());

        when(accountRepository.findByAccountNumber("123-123456-22")).thenReturn(Optional.of(icesiAccountFrom));
        when(accountRepository.findByAccountNumber("123-123456-21")).thenReturn(Optional.of(icesiAccountTo));

        try {
            accountService.transferMoney(icesiAccountFrom.getAccountNumber(), icesiAccountTo.getAccountNumber(), 100000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Deposit only accounts can't transfer or be transferred money",message);
            assertEquals("deposit only", icesiAccountFrom.getType());
        }

    }

    @Test
    public void testTransferMoneyWhenGoesToDepositOnlyAccount() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(defaultIcesiAccount());
        IcesiAccount icesiAccountFrom = accountService.save(defaultAccountCreateDTO());


        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountDepositOnly());
        IcesiAccount icesiAccountTo = accountService.save(accountCreateDTODepositOnly());


        when(accountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(icesiAccountFrom));
        when(accountRepository.findByAccountNumber("123-123456-22")).thenReturn(Optional.of(icesiAccountTo));

        try {
            accountService.transferMoney(icesiAccountFrom.getAccountNumber(), icesiAccountTo.getAccountNumber(), 100000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Deposit only accounts can't transfer or be transferred money",message);
            assertEquals("deposit only", icesiAccountTo.getType());
        }

    }

    @Test
    public void testTransferMoneyWhenAmountGreaterThanBalance() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(defaultIcesiAccount());
        IcesiAccount icesiAccountFrom = accountService.save(defaultAccountCreateDTO());


        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountToDisable());
        IcesiAccount icesiAccountTo = accountService.save(accountCreateDTOToDisable());

        when(accountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(icesiAccountFrom));
        when(accountRepository.findByAccountNumber("123-123456-21")).thenReturn(Optional.of(icesiAccountTo));

        try {
            accountService.transferMoney(icesiAccountFrom.getAccountNumber(), icesiAccountTo.getAccountNumber(), 600000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Not enough money to transfer in the account 123-123456-12",message);

        }

    }
    @Test
    public void testTransferMoneyWhenAccountIsDisable() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountDisabled());
        IcesiAccount icesiAccountFrom = accountService.save(accountCreateDTODisabled());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountToDisable());
        IcesiAccount icesiAccountTo = accountService.save(accountCreateDTOToDisable());

        when(accountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(icesiAccountFrom));
        when(accountRepository.findByAccountNumber("123-123456-21")).thenReturn(Optional.of(icesiAccountTo));

        try {
            accountService.transferMoney(icesiAccountFrom.getAccountNumber(), icesiAccountTo.getAccountNumber(), 100000);
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Transaction can't be made, account 123-123456-12 is disabled",message);

        }

    }
*/
    private IcesiAccount newIcesiAccount(){
        return IcesiAccount.builder()
                .type("normal")
                .active(true)
                .balance(0)
                .user(defaultIcesiUser())
                .build();
    }

    private AccountCreateDTO newAccountCreateDTO(){
        return AccountCreateDTO.builder()
                .type("normal")
                .active(true)
                .balance(0)
                .userEmail("julietav@example.com")
                .build();
    }

    private IcesiUser defaultIcesiUser(){

        return IcesiUser.builder()
                .firstName("Julieta")
                .lastName("Venegas")
                .email("julietav@example.com")
                .phoneNumber("3184441232")
                .password("julieta123")
                .role(defaultIcesiRole())
                .userId(UUID.randomUUID())
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("administrator")
                .description("is an administrator")
                .roleId(UUID.randomUUID())
                .build();
    }

    private IcesiAccount accountDisabled(){
        return IcesiAccount.builder()
                .type("normal")
                .active(false)
                .balance(0)
                .user(defaultIcesiUser())
                .accountNumber("123-123456-12")
                .build();
    }

    private IcesiAccount accountEnabled() {
        return IcesiAccount.builder()
                .type("normal")
                .active(true)
                .balance(0)
                .user(defaultIcesiUser())
                .accountNumber("123-123456-21")
                .build();
    }

    private IcesiAccount accountDepositOnly(){
        return IcesiAccount.builder()
                .type("deposit only")
                .active(true)
                .balance(500000)
                .user(defaultIcesiUser())
                .accountNumber("123-123456-22")
                .build();
    }

    private IcesiAccount accountNormalWithBalanceNotInZero(){
        return IcesiAccount.builder()
                .type("normal")
                .active(true)
                .balance(500000)
                .user(defaultIcesiUser())
                .accountNumber("123-123456-33")
                .build();
    }

    private TransactionDTO defaultTransactionDTO(){
        return TransactionDTO.builder()
                .accountNumberFrom(accountNormalWithBalanceNotInZero().getAccountNumber())
                .accountNumberTo(accountEnabled().getAccountNumber())
                .money(10000)
                .build();
    }

    private TransactionDTO transactionDTOWithAccountDepositOnlyFrom(){
        return TransactionDTO.builder()
                .accountNumberFrom(accountDepositOnly().getAccountNumber())
                .accountNumberTo(accountEnabled().getAccountNumber())
                .money(10000)
                .build();
    }

    private TransactionDTO transactionDTOWithAccountDepositOnlyTo(){
        return TransactionDTO.builder()
                .accountNumberFrom(accountNormalWithBalanceNotInZero().getAccountNumber())
                .accountNumberTo(accountDepositOnly().getAccountNumber())
                .money(10000)
                .build();
    }

    private TransactionDTO transactionDTOWithAccountFromBalanceInZero(){
        return TransactionDTO.builder()
                .accountNumberFrom(accountEnabled().getAccountNumber())
                .accountNumberTo(accountNormalWithBalanceNotInZero().getAccountNumber())
                .money(10000)
                .build();
    }

    private TransactionDTO transactionDTOWithAccountFromDisabled(){
        return TransactionDTO.builder()
                .accountNumberFrom(accountDisabled().getAccountNumber())
                .accountNumberTo(accountNormalWithBalanceNotInZero().getAccountNumber())
                .money(10000)
                .build();
    }
}
