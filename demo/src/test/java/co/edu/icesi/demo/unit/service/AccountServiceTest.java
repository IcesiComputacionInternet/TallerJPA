package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
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

    @Test
    public void testWithdrawalMoneyWhenAccountDoesNotExists() {

        try {
            accountService.withdrawalMoney(defaultTransactionDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Transaction can't be made, active account "+defaultTransactionDTO().getAccountNumberFrom()+" not found",message);

            verify(accountRepository,times(1)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
        }

    }

    @Test
    public void testWithdrawalMoneyWhenAmountGreaterThanBalance() {
        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.of(accountEnabled()));

        try {
            accountService.withdrawalMoney(transactionDTOWithAccountFromBalanceInZero());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Not enough money in the account to do this transaction",message);

            verify(accountRepository,times(1)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
        }

    }


    @Test
    public void testDepositMoney() {
        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.of(accountEnabled()));

        TransactionDTO transactionDTO= accountService.depositMoney(defaultTransactionDTO());
        verify(accountRepository,times(1)).findByAccountNumber(any(),eq(true));
        verify(accountRepository,times(1)).save(argThat(a->a.getBalance()==10000));
        assertEquals("Deposit successfully completed",transactionDTO.getResult());


    }

    @Test
    public void testDepositMoneyWhenAccountDoesNotExists() {

        try {
            accountService.depositMoney(defaultTransactionDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Transaction can't be made, active account "+defaultTransactionDTO().getAccountNumberTo()+" not found",message);

            verify(accountRepository,times(1)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
        }

    }

    @Test
    public void testTransferMoney() {
        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.of(accountNormalWithBalanceNotInZero()),Optional.of(accountEnabled()));

        TransactionDTO transactionDTO= accountService.transferMoney(defaultTransactionDTO());
        verify(accountRepository,times(2)).findByAccountNumber(any(),eq(true));
        verify(accountRepository,times(2)).save(argThat(a->(a.getBalance()==10000 && a.getAccountNumber().equals(accountEnabled().getAccountNumber())) || (a.getBalance()==490000 && a.getAccountNumber().equals(accountNormalWithBalanceNotInZero().getAccountNumber()))));
        assertEquals("Transfer successfully completed",transactionDTO.getResult());

    }

    @Test
    public void testTransferMoneyWhenAccountFromDoesNotExists() {

        try {
            accountService.transferMoney(defaultTransactionDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Transaction can't be made, active account "+defaultTransactionDTO().getAccountNumberFrom()+" not found",message);

            verify(accountRepository,times(1)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
        }

    }
    @Test
    public void testTransferMoneyWhenAccountToDoesNotExists() {
        when(accountRepository.findByAccountNumber(defaultTransactionDTO().getAccountNumberFrom(),true)).thenReturn(Optional.of(accountNormalWithBalanceNotInZero()));

        try {
            accountService.transferMoney(defaultTransactionDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Transaction can't be made, active account "+defaultTransactionDTO().getAccountNumberTo()+" not found",message);

            verify(accountRepository,times(2)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
        }
    }

    @Test
    public void testTransferMoneyWhenComesFromDepositOnlyAccount() {
        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.of(accountDepositOnly()),Optional.of(accountEnabled()));

        try {
            accountService.transferMoney(transactionDTOWithAccountDepositOnlyFrom());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Deposit only account "+transactionDTOWithAccountDepositOnlyFrom().getAccountNumberFrom()+" can't transfer or be transferred money",message);

            verify(accountRepository,times(2)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
        }
    }

    @Test
    public void testTransferMoneyWhenGoesToDepositOnlyAccount() {
        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.of(accountNormalWithBalanceNotInZero()) ,Optional.of(accountDepositOnly()));

        try {
            accountService.transferMoney(transactionDTOWithAccountDepositOnlyTo());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Deposit only account "+transactionDTOWithAccountDepositOnlyTo().getAccountNumberTo()+" can't transfer or be transferred money",message);

            verify(accountRepository,times(2)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
        }

    }

    @Test
    public void testTransferMoneyWhenAmountGreaterThanBalance() {
        when(accountRepository.findByAccountNumber(any(),eq(true))).thenReturn(Optional.of(accountEnabled()) ,Optional.of(accountNormalWithBalanceNotInZero()));

        try {
            accountService.transferMoney(transactionDTOWithAccountFromBalanceInZero());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Not enough money in the account to do this transaction",message);

            verify(accountRepository,times(2)).findByAccountNumber(any(),eq(true));
            verify(accountRepository,never()).save(any());
        }

    }

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


}
