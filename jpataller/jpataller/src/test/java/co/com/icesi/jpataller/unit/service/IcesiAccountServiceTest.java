package co.com.icesi.jpataller.unit.service;

import co.com.icesi.jpataller.dto.IcesiAccountDTO;
import co.com.icesi.jpataller.mapper.IcesiAccountMapper;
import co.com.icesi.jpataller.mapper.IcesiAccountMapperImpl;
import co.com.icesi.jpataller.mapper.IcesiUserMapper;
import co.com.icesi.jpataller.mapper.IcesiUserMapperImpl;
import co.com.icesi.jpataller.model.IcesiAccount;
import co.com.icesi.jpataller.model.IcesiRole;
import co.com.icesi.jpataller.model.IcesiUser;
import co.com.icesi.jpataller.repository.IcesiAccountRepository;
import co.com.icesi.jpataller.repository.IcesiUserRepository;
import co.com.icesi.jpataller.service.IcesiAccountService;
import co.com.icesi.jpataller.service.IcesiUserService;
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

public class IcesiAccountServiceTest {

    private IcesiAccountMapper accountMapper;

    private IcesiAccountService accountService;

    private IcesiAccountRepository accountRepository;

    private IcesiUserRepository userRepository;

    private IcesiUserService userService;

    private IcesiUserMapper userMapper;

    @BeforeEach
    private void init(){
        userService =mock(IcesiUserService.class);
        userRepository=mock(IcesiUserRepository.class);
        accountRepository = mock(IcesiAccountRepository.class);
        accountMapper = spy(IcesiAccountMapperImpl.class);
        userMapper = spy(IcesiUserMapperImpl.class);
        accountService = new IcesiAccountService(accountMapper, accountRepository, userService, userRepository, userMapper);
    }

    @Test
    public void testCreateAccount(){

        IcesiUser icesiUser =defaultIcesiUser();
        IcesiAccountDTO accountCreateDTO = defaultAccountDTO();
        accountCreateDTO.setUserId(icesiUser.getUserId().toString());

        when(userRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        accountService.createAccount(accountCreateDTO);

        IcesiAccount icesiAccount = defaultIcesiAccount();
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
    }

    @Test
    public void testCreateAccountWhenUserDoesNotExist(){
        IcesiUser icesiUser =defaultIcesiUser();
        IcesiAccountDTO accountCreateDTO = defaultAccountDTO();
        accountCreateDTO.setUserId(icesiUser.getUserId().toString());

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        try{
            accountService.createAccount(accountCreateDTO);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe un usuario que cree esta cuenta.",exception.getMessage());
        }


    }

    @Test
    public void testCreateAccountWithNegativeBalance(){
        IcesiAccountDTO icesiAccountDTO = defaultAccountDTO();
        icesiAccountDTO.setBalance(-22222);
        when(userRepository.findById(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            accountService.createAccount(icesiAccountDTO);
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("El balance de la cuenta es menor a 0",message);
        }
    }

    @Test
    public void testFindAccountByNumber(){
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
        verify(accountRepository, times(1)).save((any()));
    }

    @Test
    public void testDisableWhenAccountDoesNotExist(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        icesiAccount.setBalance(0);
        try{
            accountService.disableAccount(icesiAccount.getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("No hay una cuenta con ese número",exception.getMessage());
        }
    }

    @Test
    public void testDIsableWhenBalanceGreaterThanZero(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.disableAccount(icesiAccount.getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("El balance de la cuenta no es 0.",exception.getMessage());
        }
    }

    @Test
    public void testEnable(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        icesiAccount.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.enableAccount(icesiAccount.getAccountNumber());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    public void testEnableWhenAccountDoesNotExist() {
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        icesiAccount.setActive(false);
        try{
            accountService.enableAccount(icesiAccount.getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe una cuenta con ese número",exception.getMessage());
        }
    }

    @Test
    public void testWithdrawal(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.withdrawMoney(icesiAccount.getAccountNumber(),1000);
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    public void testWithdrawMoneyWithAccountNotExist(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        try{
            accountService.withdrawMoney(icesiAccount.getAccountNumber(),1000L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe una cuenta con ese número",exception.getMessage());
        }

    }

    @Test
    public void testWithdrawMoneyWithDeactivatedAccount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        icesiAccount.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdrawMoney(icesiAccount.getAccountNumber(),100L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta no está activa",exception.getMessage());
        }

    }

    @Test
    public void testWithdrawMoneyWithNegativeAmount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdrawMoney(icesiAccount.getAccountNumber(),-146L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No puedes sacar 0 o menos de la cuenta",exception.getMessage());
        }

    }

    @Test
    public void testWithdrawMoneyWithZeroAmount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdrawMoney(icesiAccount.getAccountNumber(),0L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No puedes sacar 0 o menos de la cuenta",exception.getMessage());
        }

    }

    @Test
    public void testWithdrawMoneyWithInsufficientBalance(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.withdrawMoney(icesiAccount.getAccountNumber(),10000000L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No hay suficiente dinero para retirar de la cuenta.",exception.getMessage());
        }

    }

    @Test
    public void testDeposit(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.depositMoney(icesiAccount.getAccountNumber(),1000L);
        verify(accountRepository, times(1)).save(icesiAccount);
    }

    @Test
    public void testDepositWhenAccountDoesNotExist(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        try{
            accountService.depositMoney(icesiAccount.getAccountNumber(),1420L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe una cuenta con ese número",exception.getMessage());
        }

    }

    @Test
    public void testDepositWithNegativeAmount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.depositMoney(icesiAccount.getAccountNumber(),-943L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("El valor a depositar debe ser mayor de 0",exception.getMessage());
        }

    }

    @Test
    public void testDepositWithAmount0(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.depositMoney(icesiAccount.getAccountNumber(),0L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("El valor a depositar debe ser mayor de 0",exception.getMessage());
        }

    }

    @Test
    public void testDepositWithDeactivatedAccount(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        icesiAccount.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try{
            accountService.depositMoney(icesiAccount.getAccountNumber(),100L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta no está activa",exception.getMessage());
        }

    }

    @Test
    public void testTransfer(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount icesiAccount2 = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(accountRepository.findByAccountNumber(icesiAccount2.getAccountNumber())).thenReturn(Optional.of(icesiAccount2));
        accountService.transferMoney(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber(),100L);
        verify(accountRepository, times(2)).save(any());
    }

    @Test
    public void testTransferWithOriginAccountNotExist(){

        IcesiAccount originAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount destinyAccount = defaultIcesiAccountWithNumberAndID();

        when(accountRepository.findByAccountNumber(originAccount.getAccountNumber())).thenReturn(Optional.empty());
        when(accountRepository.findByAccountNumber(destinyAccount.getAccountNumber())).thenReturn(Optional.of(destinyAccount));

        try{
            accountService.transferMoney(originAccount.getAccountNumber(),destinyAccount.getAccountNumber(),132L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta de origen no existe",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithDestinyAccountNotExist(){

        IcesiAccount originAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount destinyAccount = defaultIcesiAccountWithNumberAndID();

        when(accountRepository.findByAccountNumber(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.findByAccountNumber(destinyAccount.getAccountNumber())).thenReturn(Optional.empty());

        try{
            accountService.transferMoney(originAccount.getAccountNumber(),destinyAccount.getAccountNumber(),110L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta de destino no existe",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithOriginAccountReadOnly(){

        IcesiAccount originAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount destinyAccount = defaultIcesiAccountWithNumberAndID();
        originAccount.setType("deposit only");
        when(accountRepository.findByAccountNumber(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.findByAccountNumber(destinyAccount.getAccountNumber())).thenReturn(Optional.of(destinyAccount));

        try{
            accountService.transferMoney(originAccount.getAccountNumber(),destinyAccount.getAccountNumber(),145L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta de origen es del tipo solo deposito",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithDestinyAccountReadOnly(){

        IcesiAccount originAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount destinyAccount = defaultIcesiAccountWithNumberAndID();
        destinyAccount.setType("deposit only");
        when(accountRepository.findByAccountNumber(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.findByAccountNumber(destinyAccount.getAccountNumber())).thenReturn(Optional.of(destinyAccount));

        try{
            accountService.transferMoney(originAccount.getAccountNumber(),destinyAccount.getAccountNumber(),120L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta de destino es del tipo solo deposito",exception.getMessage());
        }
    }


    @Test
    public void testTransferWithOriginAccountBalanceInsufficient(){

        IcesiAccount originAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount destinyAccount = defaultIcesiAccountWithNumberAndID();
        originAccount.setBalance(10L);
        when(accountRepository.findByAccountNumber(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.findByAccountNumber(destinyAccount.getAccountNumber())).thenReturn(Optional.of(destinyAccount));

        try{
            accountService.transferMoney(originAccount.getAccountNumber(),destinyAccount.getAccountNumber(),100000043L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta de origen no tiene dinero suficiente",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithOriginAccountDeactivated(){

        IcesiAccount originAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount destinyAccount = defaultIcesiAccountWithNumberAndID();
        originAccount.setActive(false);
        when(accountRepository.findByAccountNumber(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.findByAccountNumber(destinyAccount.getAccountNumber())).thenReturn(Optional.of(destinyAccount));

        try{
            accountService.transferMoney(originAccount.getAccountNumber(),destinyAccount.getAccountNumber(),10000L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta de origen está desactivada",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithDestinyAccountDeactivated(){

        IcesiAccount originAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount destinyAccount = defaultIcesiAccountWithNumberAndID();
        destinyAccount.setActive(false);
        when(accountRepository.findByAccountNumber(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.findByAccountNumber(destinyAccount.getAccountNumber())).thenReturn(Optional.of(destinyAccount));

        try{
            accountService.transferMoney(originAccount.getAccountNumber(),destinyAccount.getAccountNumber(),1000L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cuenta de destino está desactivada",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithNegativeAmount(){

        IcesiAccount originAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount destinyAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.findByAccountNumber(destinyAccount.getAccountNumber())).thenReturn(Optional.of(destinyAccount));

        try{
            accountService.transferMoney(originAccount.getAccountNumber(),destinyAccount.getAccountNumber(),-29439L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cantidad de dinero debe ser mayor que 0.",exception.getMessage());
        }
    }

    @Test
    public void testTransferWithZeroAmount(){

        IcesiAccount originAccount = defaultIcesiAccountWithNumberAndID();
        IcesiAccount destinyAccount = defaultIcesiAccountWithNumberAndID();
        when(accountRepository.findByAccountNumber(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.findByAccountNumber(destinyAccount.getAccountNumber())).thenReturn(Optional.of(destinyAccount));

        try{
            accountService.transferMoney(originAccount.getAccountNumber(),destinyAccount.getAccountNumber(),0L);
            fail();
        }catch (RuntimeException exception){
            assertEquals("La cantidad de dinero debe ser mayor que 0.",exception.getMessage());
        }
    }
    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("gabreldelgado2002@gmail.com")
                .firstName("Gabriel")
                .lastName("Delgado")
                .phoneNumber("123456")
                .password("123456")
                .accounts(new ArrayList<>())
                .role(IcesiRole.builder()
                        .name("Rol 1")
                        .description("Rol de prueba")
                        .build())
                .build();
    }

    private IcesiAccount defaultIcesiAccountWithNumberAndID(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber(accountService.generateNumber())
                .active(true)
                .balance(52000)
                .type("joe mama")
                .build();
    }




    private IcesiAccountDTO defaultAccountDTO(){
        return IcesiAccountDTO.builder()
                .userId("e5b698fe-3d65-4b55-b2b4-f38a3c91ccd0")
                .active(true)
                .balance(52000)
                .type("joe mama")
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .active(true)
                .balance(52000)
                .type("joe mama")
                .accountOwner(IcesiUser.builder()
                        .email("gabreldelgado2002@gmail.com")
                        .firstName("Gabriel")
                        .lastName("Delgado")
                        .phoneNumber("123456")
                        .password("123456")
                        .role(IcesiRole.builder()
                                .name("Rol 1")
                                .description("Rol de prueba")
                                .build())
                        .build())
                .build();
    }
}

