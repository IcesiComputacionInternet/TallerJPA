package com.Icesi.TallerJPA.unit.service;

import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import com.Icesi.TallerJPA.dto.IcesiTransactionsDTO;
import com.Icesi.TallerJPA.mapper.IcesiAccountMapper;

import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.Icesi.TallerJPA.repository.IcesiAccountRepository;
import com.Icesi.TallerJPA.service.IcesiAccountService;
import com.Icesi.TallerJPA.unit.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.Icesi.TallerJPA.mapper.IcesiAccountMapperImpl;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiAccountServiceTest {

    private IcesiAccountService icesiAccountService;

    private IcesiAccountRepository icesiAccountRepository;

    private IcesiAccountMapper accountMapper;

    private IcesiAccount icesiAccount;
    private  IcesiAccountMapper icesiAccountMapper;
    @BeforeEach
    private void init() {
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        accountMapper = spy(IcesiAccountMapperImpl.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, accountMapper);
        icesiAccount = createIcesiAccount();
    }
    


    @Test
    public void validateAccountNumber(){
        when(accountMapper.fromIcesiAccountDTO(any())).thenReturn(createIcesiAccount());

        IcesiAccountDTO accountDTO = icesiAccountDTO();

        icesiAccountService.save(accountDTO);

        verify(icesiAccountRepository, times(1)).save(any());

        String accountNumber = accountDTO.getAccountNumber();

        boolean verificationOfAccountNumber = Arrays.stream(accountNumber.split("-")).allMatch(symbol -> Pattern.matches("\\d+",symbol));

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
        assertFalse(icesiAccountService.disableAccount( icesiDtoWithBalanceZero.getAccountNumber()).isActive());

    }


    @Test
    public void testEnableExistingAccount() {

        IcesiAccountDTO icesiAccountDisableDto = createicesiAccountDisableDto();
        icesiAccountService.save(icesiAccountDisableDto);
        String accountNumber = icesiAccountDisableDto.getAccountNumber();
        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(icesiAccount));
        assertTrue(icesiAccountService.enableAccount( icesiAccountDisableDto.getAccountNumber()).isActive());
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
        String accountNumberOficesiAccountDeposit =null;
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
}
