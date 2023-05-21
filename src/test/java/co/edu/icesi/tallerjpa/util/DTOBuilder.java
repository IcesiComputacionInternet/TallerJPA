package co.edu.icesi.tallerjpa.util;

import co.edu.icesi.tallerjpa.dto.*;
import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;

public class DTOBuilder {
    public static IcesiRoleCreateDTO defaultIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Role for demo")
                .name(NameIcesiRole.USER.toString())
                .build();

    }

    public static IcesiRoleCreateDTO adminIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Role for demo")
                .name(NameIcesiRole.ADMIN.toString())
                .build();

    }

    public static IcesiUserCreateDTO defaultIcesiUserCreateDTO(){
        return IcesiUserCreateDTO.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRoleCreateDTO(defaultIcesiRoleCreateDTO())
                .build();
    }

    public static IcesiUserCreateDTO adminIcesiUserCreateDTO(){
        return IcesiUserCreateDTO.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRoleCreateDTO(adminIcesiRoleCreateDTO())
                .build();
    }

    public static IcesiAccountCreateDTO regularIcesiAccountCreateDTO(){
        return IcesiAccountCreateDTO.builder()
                .balance(0)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT)
                .active(true)
                .icesiUserEmail(defaultIcesiUserCreateDTO().getEmail())
                .build();
    }

    public static TransactionCreateDTO defaultTransferTransactionCreateDTO(){
        return TransactionCreateDTO.builder()
                .senderAccountNumber("111-123456-11")
                .receiverAccountNumber("222-123456-22")
                .amount(500)
                .build();
    }

    public static TransactionWithOneAccountCreateDTO defaultWithdrawalTransactionCreateDTO(){
        return TransactionWithOneAccountCreateDTO.builder()
                .accountNumber("111-123456-11")
                .amount(500)
                .build();
    }

    public static TransactionWithOneAccountCreateDTO defaultDepositTransactionCreateDTO(){
        return TransactionWithOneAccountCreateDTO.builder()
                .accountNumber("111-123456-11")
                .amount(500)
                .build();
    }
}
