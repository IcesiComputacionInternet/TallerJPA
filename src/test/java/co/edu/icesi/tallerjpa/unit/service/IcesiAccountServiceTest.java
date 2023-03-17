package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.dto.IcesiAccountCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapperImpl;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.service.IcesiAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class IcesiAccountServiceTest {

    private IcesiAccountRepository icesiAccountRepository;
    private IcesiAccountMapper icesiAccountMapper;
    private IcesiAccountService icesiAccountService;
    private IcesiUserRepository icesiUserRepository;

    private IcesiAccountCreateDTO normalIcesiAccountCreateDTO(){
        return IcesiAccountCreateDTO.builder()
                .balance(0)
                .type("normal account")
                .active(true)
                .icesiUserDTO(defaultIcesiUserCreateDTO())
                .build();
    }

    private IcesiAccount normalIcesiAccountCreateWith1000(){
        return IcesiAccount.builder()
                .balance(1000)
                .type("normal account")
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountId(null)
                .accountNumber(null)
                .balance(500)
                .type("normal account")
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }

    private IcesiUserCreateDTO defaultIcesiUserCreateDTO(){
        return IcesiUserCreateDTO.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRoleCreateDTO(defaultIcesiRoleCreateDTO())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(defaultIcesiRole())
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name("Admin")
                .build();
    }

    private IcesiRoleCreateDTO defaultIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Manage the system")
                .name("Admin")
                .build();
    }

    @BeforeEach
    private void init(){
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        icesiAccountMapper = spy(IcesiAccountMapperImpl.class);
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, icesiAccountMapper, icesiUserRepository);
    }

    @Test
    public void testCreateIcesiAccount(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiAccountService.save(normalIcesiAccountCreateDTO());
        IcesiAccount icesiAccount = IcesiAccount.builder()
                .balance(0)
                .type("normal account")
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
        verify(icesiAccountRepository, times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
    }

    @Test
    public void testGenerateUUID(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiAccountService.save(normalIcesiAccountCreateDTO());
        verify(icesiAccountRepository, times(1)).save(argThat(x -> x.getAccountId() != null));
    }

    @Test
    public void testGenerateAccountNumber(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiAccountService.save(normalIcesiAccountCreateDTO());
        verify(icesiAccountRepository, times(1)).save(argThat(x -> x.getAccountNumber() != null));
    }

    @Test
    public void testCreateIcesiAccountWithBalanceMinorZero(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = normalIcesiAccountCreateDTO();
        icesiAccountCreateDTO.setBalance(-1);
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.save(icesiAccountCreateDTO));
        assertEquals("Accounts balance can't be below 0.", exception.getMessage());
    }

    @Test
    public void testCreateIcesiAccountDisableWithBalanceDifferentZero(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = normalIcesiAccountCreateDTO();
        icesiAccountCreateDTO.setBalance(1);
        icesiAccountCreateDTO.setActive(false);
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.save(icesiAccountCreateDTO));
        assertEquals("Account can only be disabled if the balance is 0.", exception.getMessage());
    }

    @Test
    public void testEnableAccount(){
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        icesiAccountService.enableAccount(accountId);
        verify(icesiAccountRepository, times(1)).enableAccount(argThat(x -> x == accountId));
    }
    @Test
    public void disableExistingAccountWithBalanceZero(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(0);
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(icesiAccount));
        icesiAccountService.disableAccount(accountId);
        verify(icesiAccountRepository, times(1)).disableAccount(argThat(x -> x.equals(accountId)));
    }

    @Test
    public void disableExistingAccountWithBalanceDifferentOfZero(){
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.disableAccount(accountId));
        assertEquals("Account can only be disabled if the balance is 0.", exception.getMessage());
        verify(icesiAccountRepository, times(0)).disableAccount(any());
    }

    @Test
    public void disableNotExistingAccount(){
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        when(icesiUserRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        assertThrows(RuntimeException.class, () -> icesiAccountService.disableAccount(accountId));
        verify(icesiAccountRepository, times(0)).disableAccount(any());
    }

    @Test
    public void testTransferMoney(){
        String accountId1 = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        String accountId2 = "c34f11df-1234-4d75-a74b-4d8c98d6074f";
        when(icesiAccountRepository.findById(UUID.fromString(accountId1))).thenReturn(Optional.of(normalIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(accountId2))).thenReturn(Optional.of(normalIcesiAccountCreateWith1000()));
        long fromAccountBalance = icesiAccountService.transferMoney(accountId1, accountId2, 500);
        assertEquals(500, fromAccountBalance);
        verify(icesiAccountRepository, times(1)).updateBalance(longThat((x -> x == 500)), argThat(x -> x.equals(accountId1)));
        verify(icesiAccountRepository, times(1)).updateBalance(longThat((x -> x == 1500)), argThat(x -> x.equals(accountId2)));
    }

    @Test
    public void testTransferMoneyInvalidReceiverUser(){
        String accountId1 = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        String accountId2 = "c34f11df-1234-4d75-a74b-4d8c98d6074f";
        when(icesiAccountRepository.findById(UUID.fromString(accountId1))).thenReturn(Optional.of(normalIcesiAccountCreateWith1000()));
        IcesiAccount icesiAccountReceiver = normalIcesiAccountCreateWith1000();
        icesiAccountReceiver.setType("deposit only");
        icesiAccountReceiver.setAccountId(UUID.fromString(accountId2));
        when(icesiAccountRepository.findById(UUID.fromString(accountId2))).thenReturn(Optional.of(icesiAccountReceiver));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(accountId1, accountId2, 500));
        assertEquals("The account with id " + accountId2 + " is marked as deposit only so no money can be transferred", exception.getMessage());
    }

    @Test
    public void testTransferMoneyInvalidSenderUser(){
        String accountId1 = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        String accountId2 = "c34f11df-1234-4d75-a74b-4d8c98d6074f";
        IcesiAccount icesiAccountSender = normalIcesiAccountCreateWith1000();
        icesiAccountSender.setType("deposit only");
        icesiAccountSender.setAccountId(UUID.fromString(accountId1));
        when(icesiAccountRepository.findById(UUID.fromString(accountId1))).thenReturn(Optional.of(icesiAccountSender));
        when(icesiAccountRepository.findById(UUID.fromString(accountId2))).thenReturn(Optional.of(normalIcesiAccountCreateWith1000()));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(accountId1, accountId2, 500));
        assertEquals("The account with id " + accountId1 + " is marked as deposit only so it can't transfers money", exception.getMessage());
    }

    @Test
    public void testTransferWithNotEnoughMoney(){
        String accountId1 = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        String accountId2 = "c34f11df-1234-4d75-a74b-4d8c98d6074f";
        IcesiAccount icesiAccount = normalIcesiAccountCreateWith1000();
        when(icesiAccountRepository.findById(UUID.fromString(accountId1))).thenReturn(Optional.of(normalIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(accountId2))).thenReturn(Optional.of(normalIcesiAccountCreateWith1000()));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(accountId1, accountId2, 1001));
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("Not enough money to transfer. At most you can transfer: " + icesiAccount.getBalance(), exception.getMessage());
    }
}
