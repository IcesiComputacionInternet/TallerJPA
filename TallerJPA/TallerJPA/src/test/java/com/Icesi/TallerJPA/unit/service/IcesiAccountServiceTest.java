package com.Icesi.TallerJPA.unit.service;

import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import com.Icesi.TallerJPA.dto.IcesiTransactionsDTO;
import com.Icesi.TallerJPA.mapper.IcesiAccountMapper;

import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.Icesi.TallerJPA.repository.IcesiAccountRepository;
import com.Icesi.TallerJPA.service.IcesiAccountService;
import com.Icesi.TallerJPA.unit.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.Icesi.TallerJPA.mapper.IcesiAccountMapperImpl;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiAccountServiceTest {

    private IcesiAccountService icesiAccountService;

    private IcesiAccountRepository icesiAccountRepository;

    private IcesiAccountMapper accountMapper;

    private IcesiAccount icesiAccount;
    private IcesiAccountMapper icesiAccountMapper;

    @BeforeEach
    private void init() {
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        accountMapper = spy(IcesiAccountMapperImpl.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, accountMapper);
        icesiAccount = createIcesiAccount();
    }


    @Test
    public void validateAccountNumber() {
        when(accountMapper.fromIcesiAccountDTO(any())).thenReturn(createIcesiAccount());

        IcesiAccountDTO accountDTO = icesiAccountDTO();

        icesiAccountService.save(accountDTO);

        verify(icesiAccountRepository, times(1)).save(any());

        String accountNumber = accountDTO.getAccountNumber();

        boolean verificationOfAccountNumber = Arrays.stream(accountNumber.split("-")).allMatch(symbol -> Pattern.matches("\\d+", symbol));

        assertTrue(verificationOfAccountNumber);
    }




    @Test
    public void testCreateAccountWithBalanceBelowZero() {

        when(accountMapper.fromIcesiAccountDTO(any())).thenReturn(createIcesiAccount());

        try {
            icesiAccountService.save(createAccountDTOWithBalanceBelowZero());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("YOU CANNOT CREATE AN ACCOUNT WITH BALANCE LESS OR EQUAL TO 0", messageOfException);
        }
    }

    @Test
    public void testAccountDisable() {

        IcesiAccountDTO icesiDtoWithBalanceZero = createicesiDtoWithBalanceZero();

        icesiAccountService.save(icesiDtoWithBalanceZero);

        String accountNumber = icesiDtoWithBalanceZero.getAccountNumber();

        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(icesiAccount));

        verify(icesiAccountRepository, times(1)).save(any());
        assertFalse(icesiAccountService.disableAccount(icesiDtoWithBalanceZero.getAccountNumber()).isActive());

    }


    @Test
    public void testEnableExistingAccount() {

        IcesiAccountDTO icesiAccountDisableDto = createicesiAccountDisableDto();
        icesiAccountService.save(icesiAccountDisableDto);
        String accountNumber = icesiAccountDisableDto.getAccountNumber();
        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(icesiAccount));
        assertTrue(icesiAccountService.enableAccount(icesiAccountDisableDto.getAccountNumber()).isActive());
    }

    @Test
    public void testSetStatusNotExistingAccount() {

        IcesiAccountDTO icesiAccountCreateDto = icesiAccountDTO();
        icesiAccountCreateDto.setAccountNumber("03-342886-00");

        when(icesiAccountRepository.findByAccountNumber(icesiAccountCreateDto.getAccountNumber())).thenReturn(Optional.empty());
        verify(icesiAccountRepository, times(0)).save(any());

        try {
            icesiAccountService.enableAccount(icesiAccountCreateDto.getAccountNumber());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("ACCOUNT NOT FOUND", messageOfException);
        }

    }


    @Test
    public void testDepositMoneyDepositOnlyAccount() {

        IcesiAccountDTO icesiAccountDeposit = createicesiDtoWithBalanceZero();

        icesiAccountService.save(icesiAccountDeposit);

        verify(icesiAccountRepository, times(1)).save(any());

        icesiAccountService.save(icesiAccountDeposit);

        String accountNumberOficesiAccountDeposit = icesiAccountDeposit.getAccountNumber();

        when(icesiAccountRepository.findByAccountNumber(accountNumberOficesiAccountDeposit)).thenReturn(Optional.of(createIcesiAccount()));

        icesiAccountService.depositCash(icesiAccountDeposit, accountNumberOficesiAccountDeposit, 8000);

        assertEquals(8000, icesiAccountDeposit.getBalance());
    }


    @Test
    public void testDepositMoneyDepositWhenTheAccountNumberDoesNotExist() {

        IcesiAccountDTO icesiAccountDeposit = createicesiDtoWithBalanceZero();
        icesiAccountService.save(icesiAccountDeposit);
        verify(icesiAccountRepository, times(1)).save(any());
        icesiAccountService.save(icesiAccountDeposit);
        String accountNumberOficesiAccountDeposit = null;
        try {
            icesiAccountService.depositCash(icesiAccountDeposit, accountNumberOficesiAccountDeposit, 8000);
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("ACCOUNT NOT FOUND", messageOfException);

        }
    }


    @Test
    public void testNormalTransferInvalidValue() {

        IcesiAccountDTO sourceAccount = createicesiAccountDTONormal();
        IcesiAccountDTO destinationAccount = createicesiAccountDTONormal1();

        icesiAccountService.save(sourceAccount);
        icesiAccountService.save(destinationAccount);

        verify(icesiAccountRepository, times(2)).save(any());

        when(icesiAccountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(icesiAccountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            icesiAccountService.sendMoney(sourceAccount, destinationAccount, 0);
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("INVALID VALUE", messageOfException);

        }
    }


    @Test
    public void tesTransferInvalidTypeAccount() {

        IcesiAccountDTO sourceAccount = createicesiAccountDTONTypeInvalid();
        IcesiAccountDTO destinationAccount = createicesiAccountDTONormal1();

        icesiAccountService.save(sourceAccount);
        icesiAccountService.save(destinationAccount);

        verify(icesiAccountRepository, times(2)).save(any());

        when(icesiAccountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(icesiAccountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            icesiAccountService.sendMoney(sourceAccount, destinationAccount, 1000);
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("THIS TYPE OF ACCOUNT DOES NOT ALLOW TO MAKE TRANSFERS", messageOfException);

        }
    }








    @Test
    public void tesTransferValid() {
        IcesiAccount account = defaultAccount();
        IcesiAccount account2 = defaultAccount2();
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account2));
        try {
            icesiAccountService.transfer(IcesiTransactionsDTO.builder()
                    .accountOrigin("1234567")
                    .accountDestination("12345678")
                    .amount(5L)
                    .build()
            );
        } catch (RuntimeException e) {
            assertEquals("THIS TYPE OF ACCOUNT DOES NOT ALLOW TO MAKE TRANSFERS", e.getMessage());
        }

        assertEquals(account.getBalance(), account.getBalance());

    }
    @Test
    public void tesTransferTypeInvalid() {
        IcesiAccount account = accountInvalid();

        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        try {
            icesiAccountService.transfer(IcesiTransactionsDTO.builder()
                    .accountOrigin("1234567")
                    .amount(5L)
                    .build()
            );
        } catch (RuntimeException e) {
            assertEquals("THIS TYPE OF ACCOUNT DOES NOT ALLOW TO MAKE TRANSFERS", e.getMessage());
        }



    }

    @Test
    public void tesTransferInsufficientMoney() {
        IcesiAccount account = defaultAccount();
        IcesiAccount account2 = defaultAccount2();
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account2));
        try {
            icesiAccountService.transfer(IcesiTransactionsDTO.builder()
                    .accountOrigin("1234567")
                    .accountDestination("12345678")
                    .amount(50000000L)
                    .build()
            );
        } catch (RuntimeException e) {
            assertEquals("INSUFFICIENT BALANCE", e.getMessage());
        }

        assertEquals(account.getBalance(), account.getBalance());
    }






    @Test
    public void testTransferValueValid() {

        IcesiAccountDTO sourceAccount = createicesiAccountDTONormal();
        IcesiAccountDTO destinationAccount = createicesiAccountDTONormal1();

        icesiAccountService.save(sourceAccount);
        icesiAccountService.save(destinationAccount);

        verify(icesiAccountRepository, times(2)).save(any());

        when(icesiAccountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(icesiAccountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            icesiAccountService.sendMoney(sourceAccount, destinationAccount, 1000);
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals(6000, sourceAccount.getBalance());

        }
    }

    @Test
    public void testTransferInsufficientBalance() {

        IcesiAccountDTO sourceAccount = createicesiAccountDTONormal();
        IcesiAccountDTO destinationAccount = createicesiAccountDTONormal1();

        icesiAccountService.save(sourceAccount);
        icesiAccountService.save(destinationAccount);

        verify(icesiAccountRepository, times(2)).save(any());

        when(icesiAccountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(icesiAccountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            icesiAccountService.sendMoney(sourceAccount, destinationAccount, 80000);
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("INSUFFICIENT BALANCE", messageOfException);

        }
    }



    @Test
    public void TransferTestDepositAccount() {
        IcesiAccountDTO sourceAccount = createicesiAccountDTONormal1();
        IcesiAccountDTO destinationDepositOnlyAccount = icesiAccountDTO();

        icesiAccountService.save(sourceAccount);
        icesiAccountService.save(destinationDepositOnlyAccount);

        verify(icesiAccountRepository, times(2)).save(any());

        when(icesiAccountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(icesiAccountRepository.findByAccountNumber(destinationDepositOnlyAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            icesiAccountService.sendMoney(sourceAccount, destinationDepositOnlyAccount, 1000);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("THIS TYPE OF ACCOUNT DOES NOT ALLOW TO MAKE TRANSFERS", messageOfException);
        }
    }




    public IcesiAccountDTO icesiAccountDTO() {
        return IcesiAccountDTO.builder()
                .balance(Long.valueOf(5000))
                .type("Deposit only")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiAccountDTO createicesiDtoWithBalanceZero() {
        return IcesiAccountDTO.builder()
                .balance(Long.valueOf(0))
                .type("Deposit only")
                .active(true)
                .build();
    }

    public IcesiAccountDTO createicesiAccountDTONormal() {
        return IcesiAccountDTO.builder()
                .balance(Long.valueOf(5000))
                .type("Normal")
                .active(true)
                .build();
    }

    public IcesiAccountDTO createicesiAccountDTONormal1() {
        return IcesiAccountDTO.builder()
                .balance(Long.valueOf(7000))
                .type("Normal")
                .active(true)
                .build();
    }
    public IcesiAccountDTO createicesiAccountDTONTypeInvalid() {
        return IcesiAccountDTO.builder()
                .balance(Long.valueOf(7000))
                .type("Deposit Only")
                .active(true)
                .build();
    }

    public IcesiAccountDTO createicesiAccountDisableDto() {
        return IcesiAccountDTO.builder()
                .balance(5L)
                .type("Deposit Only")
                .active(false)
                .build();
    }

    public IcesiAccountDTO createAccountDTOWithBalanceBelowZero() {
        return IcesiAccountDTO.builder()
                .balance(Long.valueOf(-1))
                .type("Deposit only")
                .active(true)
                .build();
    }

    public IcesiAccount createIcesiAccount() {
        return IcesiAccount.builder()
                .balance(5000)
                .type("Normal")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiUser createIcesiUser() {
        return IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("1234567")
                .password("1234")
                .build();
    }
    private IcesiAccount accountInvalid() {
        return IcesiAccount.builder()
                .accountNumber("1234567")
                .balance(50L)
                .type("Deposit only")
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }
    private IcesiAccount defaultAccount() {
        return IcesiAccount.builder()
                .accountNumber("1234567")
                .balance(50L)
                .type("Normal")
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }
    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c0a81201-0000-0000-0000-000000000000"))
                .firstName("Luis")
                .lastName("David")
                .email("example@exampleEmail.com")
                .phoneNumber("1234545")
                .password("1234125")
                .icesiRole(defaultRole())
                .build();
    }
    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.fromString("c0a81201-0000-0000-0000-000000000000"))
                .name("Estudiante")
                .description("Sistemas")
                .build();
    }


    private IcesiAccount defaultAccount2() {
        return IcesiAccount.builder()
                .accountNumber("12345678")
                .balance(50L)
                .type("Normal")
                .active(true)
                .icesiUser(defaultIcesiUser2())
                .build();
    }
    private IcesiUser defaultIcesiUser2(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c0a80101-0000-0000-0000-000000200000"))
                .firstName("Luis")
                .lastName("David")
                .email("examplea@exampleEmail.com")
                .phoneNumber("1234545")
                .password("1234125")
                .icesiRole(defaultRole2())
                .build();
    }
    private IcesiRole defaultRole2(){
        return IcesiRole.builder()
                .roleId(UUID.fromString("c0a80101-0000-0000-0000-000000200000"))
                .name("Estudiante")
                .description("Sistemas")
                .build();
    }
}
