package co.com.icesi.demojpa.unit.service;


import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.TransactionDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.mapper.AccountMapperImpl;
import co.com.icesi.demojpa.mapper.response.AccountResponseMapper;
import co.com.icesi.demojpa.mapper.response.AccountResponseMapperImpl;
import co.com.icesi.demojpa.model.IcesiAccount;

import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.servicio.AccountService;

import co.com.icesi.demojpa.servicio.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AccountServiceTest {

    private AccountMapper accountMapper;

    private AccountService accountService;

    private AccountRepository  accountRepository;

    private UserRepository userRepository;

    private UserService userService;

    private AccountResponseMapper accountResponseMapper;

    @BeforeEach
    private void init(){
        userService =mock(UserService.class);
        userRepository=mock(UserRepository.class);
        accountRepository = mock(AccountRepository.class);
        accountResponseMapper = spy(AccountResponseMapperImpl.class);
        accountMapper = spy(AccountMapperImpl.class);
        accountService = new AccountService(accountRepository, userRepository, userService, accountMapper, accountResponseMapper);
    }

    @Test
    public void testCreateAccount(){

        IcesiUser icesiUser =defaultIcesiUser();
        AccountCreateDTO accountCreateDTO = defaultAccountDTO();
        accountCreateDTO.setUserId(icesiUser.getUserId().toString());

        when(userRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        accountService.save(accountCreateDTO);

        IcesiAccount icesiAccount = defaultIcesiAccount();
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
    }

    @Test
    public void testCreateAccountWithNoValidUser(){
        IcesiUser icesiUser =defaultIcesiUser();
        AccountCreateDTO accountCreateDTO = defaultAccountDTO();
        accountCreateDTO.setUserId(icesiUser.getUserId().toString());

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        try{
            accountService.save(accountCreateDTO);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe una cuenta con esta id",exception.getMessage());
        }


    }

    @Test
    public void testCreateAccountWithNegativeBalance(){
        AccountCreateDTO accountCreateDTO = defaultAccountDTO();
        accountCreateDTO.setBalance(-22222);
        try{
            accountService.save(accountCreateDTO);
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("El balance no puede ser menor a 0",message);
        }
    }

    @Test
    public void testFindByNumber(){
        IcesiAccount account =defaultIcesiAccountWithNumberAndID();
        accountRepository.save(account);
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        Optional<IcesiAccount> account2 = accountRepository.findByAccountNumber(account.getAccountNumber());
        assertEquals(account2.get().getAccountNumber(),account.getAccountNumber());
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testDisable(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        icesiAccount.setBalance(0);
        accountService.disableAccount(icesiAccount.getAccountNumber());
        verify(accountRepository, times(1)).disableAccount(any());
    }

    @Test
    public void testDisableWithOutAccount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        icesiAccount.setBalance(0);
        try{
            accountService.disableAccount(icesiAccount.getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe una cuenta con este numero",exception.getMessage());
        }
    }

    @Test
    public void testDisableWithOutBalance0(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.disableAccount(icesiAccount.getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("El balance de esta cuenta no es 0",exception.getMessage());
        }
    }

    @Test
    public void testEnable(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        icesiAccount.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.enableAccount(icesiAccount.getAccountNumber());
        verify(accountRepository, times(1)).enableAccount(any());
    }

    @Test
    public void testEnableWithActiveAccount() {
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try {
            accountService.enableAccount(icesiAccount.getAccountNumber());
            fail();
        } catch (RuntimeException exception){
            assertEquals("La cuenta ya esta activada",exception.getMessage());
        }
    }

    @Test
    public void testEnableWithAOutAccount() {
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        icesiAccount.setActive(false);
        try{
            accountService.enableAccount(icesiAccount.getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe una cuenta con este numero",exception.getMessage());
        }
    }

    @Test
    public void testWithdrawal(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.withdrawal(icesiAccount.getAccountNumber(),1000);
        verify(accountRepository, times(1)).updateBalance(eq(icesiAccount.getAccountNumber()),eq(icesiAccount.getBalance()-1000L));
    }

    @Test
    public void testWithdrawalWithOutAccount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        try{
            accountService.withdrawal(icesiAccount.getAccountNumber(),1000L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe una cuenta con este numero",exception.getMessage());
        }

    }

    @Test
    public void testWithdrawalWithUnActiveAccount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        icesiAccount.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdrawal(icesiAccount.getAccountNumber(),1000L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta no esta activa",exception.getMessage());
        }

    }

    @Test
    public void testWithdrawalWithNegativeWithdrawal(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdrawal(icesiAccount.getAccountNumber(),-500L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cantidad de dinero que se quiere sacar debe ser mayor que 0",exception.getMessage());
        }

    }

    @Test
    public void testWithdrawalWithZeroWithdrawal(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdrawal(icesiAccount.getAccountNumber(),0L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cantidad de dinero que se quiere sacar debe ser mayor que 0",exception.getMessage());
        }

    }

    @Test
    public void testWithdrawalWithNotEnoughBalance(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdrawal(icesiAccount.getAccountNumber(),1000000L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No hay balance suficiente para sacar",exception.getMessage());
        }

    }

    @Test
    public void testDeposit(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.deposit(icesiAccount.getAccountNumber(),1000L);
        verify(accountRepository, times(1)).updateBalance(eq(icesiAccount.getAccountNumber()),eq(icesiAccount.getBalance()+1000L));
    }

    @Test
    public void testDepositWithOutAccount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        try{
            accountService.deposit(icesiAccount.getAccountNumber(),1000L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe una cuenta con este numero",exception.getMessage());
        }

    }

    @Test
    public void testDepositWithNegativeDeposit(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.deposit(icesiAccount.getAccountNumber(),-500L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cantidad de dinero que se quiere depositar debe ser mayor que 0",exception.getMessage());
        }

    }

    @Test
    public void testDepositWithZeroDeposit(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.deposit(icesiAccount.getAccountNumber(),0L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cantidad de dinero que se quiere depositar debe ser mayor que 0",exception.getMessage());
        }

    }

    @Test
    public void testDepositWithUnActiveAccount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        icesiAccount.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.deposit(icesiAccount.getAccountNumber(),1000L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta no esta activa",exception.getMessage());
        }

    }

    @Test
    public void testTransfer(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));
        accountService.transfer(TransactionDTO.builder()
                .accountNumberOrigin("1234567899")
                .accountNumberDestination("123456789")
                .amount(1000L)
                .resultMessage("")
                .build());
        verify(accountRepository, times(1)).updateBalance(eq(icesiAccount.getAccountNumber()),eq(icesiAccount.getBalance()-100L));
        verify(accountRepository, times(1)).updateBalance(eq(icesiAccount2.getAccountNumber()),eq(icesiAccount.getBalance()+100L));
    }

    @Test
    public void testTransferWithOutTransferringAccount(){

        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();

        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.empty());
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));

        try{
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("1234567899")
                    .accountNumberDestination("123456789")
                    .amount(1000L)
                    .resultMessage("")
                    .build());
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta que manda el dinero no existe",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithOutReceivingAccount(){

        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();

        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.empty());

        try{
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("1234567899")
                    .accountNumberDestination("123456789")
                    .amount(1000L)
                    .resultMessage("")
                    .build());
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta que recibe el dinero no existe",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithTransferringAccountDepositOnly(){

        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();
        icesiAccount.setType("deposit only");
        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));

        try{
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("1234567899")
                    .accountNumberDestination("123456789")
                    .amount(1000L)
                    .resultMessage("")
                    .build());
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta que manda el dinero es de tipo 'deposit only' ",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithReceivingAccountDepositOnly(){

        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();
        icesiAccount2.setType("deposit only");
        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));

        try{
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("1234567899")
                    .accountNumberDestination("123456789")
                    .amount(1000L)
                    .resultMessage("")
                    .build());
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta que recibe el dinero es de tipo 'deposit only' ",exception.getMessage());
        }
    }


    @Test
    public void testTransferWithTransferringAccountNotEnoughBalance(){

        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();
        icesiAccount.setBalance(10L);
        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));

        try{
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("1234567899")
                    .accountNumberDestination("123456789")
                    .amount(1000L)
                    .resultMessage("")
                    .build());
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta que manda el dinero no tiene balance suficiente ",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithTransferringAccountIsNotActive(){

        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();
        icesiAccount.setActive(false);
        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));

        try{
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("1234567899")
                    .accountNumberDestination("123456789")
                    .amount(1000L)
                    .resultMessage("")
                    .build());
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta que manda el dinero no esta activa",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithReceivingAccountIsNotActive(){

        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();
        icesiAccount2.setActive(false);
        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));

        try{
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("1234567899")
                    .accountNumberDestination("123456789")
                    .amount(1000L)
                    .resultMessage("")
                    .build());
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta que recibe el dinero no esta activa",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithNegativeTransferAmount(){

        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));

        try{
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("1234567899")
                    .accountNumberDestination("123456789")
                    .amount(1000L)
                    .resultMessage("")
                    .build());
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cantidad de dinero que se quiere enviar debe ser mayor que 0",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithZeroTransferAmount(){

        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));

        try{
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("1234567899")
                    .accountNumberDestination("123456789")
                    .amount(1000L)
                    .resultMessage("")
                    .build());
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cantidad de dinero que se quiere enviar debe ser mayor que 0",exception.getMessage());
        }
    }



    private AccountCreateDTO defaultAccountDTO(){
        return AccountCreateDTO.builder()
                .userId("")
                .active(true)
                .balance(10000)
                .type("polish emissary")
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("5")
                .firstName("John")
                .lastName("Doe")
                .phone("123")
                .password("123")
                .accounts(new ArrayList<>())
                .role(IcesiRole.builder()
                        .name("rol de prueba")
                        .description("rol de prueba")
                        .build())
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .active(true)
                .balance(10000)
                .type("polish emissary")
                .account(IcesiUser.builder()
                        .email("5")
                        .firstName("John")
                        .lastName("Doe")
                        .phone("123")
                        .password("123")
                        .role(IcesiRole.builder()
                                .name("rol de prueba")
                                .description("rol de prueba")
                                .build())
                        .build())
                .build();
    }
    private IcesiAccount defaultIcesiAccountWithNumberAndID(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber(accountService.genNumber())
                .active(true)
                .balance(10000)
                .type("polish emissary")
                .build();
    }
}


