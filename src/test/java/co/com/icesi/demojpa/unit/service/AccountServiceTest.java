package co.com.icesi.demojpa.unit.service;


import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.mapper.AccountMapperImpl;
import co.com.icesi.demojpa.model.IcesiAccount;

import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.servicio.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    private void init(){
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        accountService = new AccountService(accountRepository,accountMapper);
    }

    @Test
    public void testCreateAccount(){
        accountService.save(defaultAccountDTO());
        IcesiAccount icesiAccount = defaultIcesiAccount();
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
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
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
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
        accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),100L);
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
            accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),100L);
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
            accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),100L);
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
            accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),100L);
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
            accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),100L);
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
            accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),100L);
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
            accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),100L);
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
            accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),100L);
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
            accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),-100L);
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
            accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),0L);
        }catch (RuntimeException exception){
            assertEquals("La cantidad de dinero que se quiere enviar debe ser mayor que 0",exception.getMessage());
        }
    }



    private AccountCreateDTO defaultAccountDTO(){
        return AccountCreateDTO.builder()
                .active(true)
                .balance(10000)
                .type("polish emissary")
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .active(true)
                .balance(10000)
                .type("polish emissary")
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


