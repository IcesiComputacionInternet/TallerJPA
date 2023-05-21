package co.edu.icesi.tallerjpa.util;

import co.edu.icesi.tallerjpa.dto.IcesiAccountCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.TransactionCreateDTO;
import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;

public class DTOBuilder {
    public static IcesiRoleCreateDTO defaultIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Manage the system")
                .name(NameIcesiRole.USER.toString())
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
                .senderAccountId("c34f11df-cda3-4d75-a74b-4d8c98d6074f")
                .receiverAccountId("c34f11df-1234-4d75-a74b-4d8c98d6074f")
                .amount(500)
                .build();
    }

    public static TransactionCreateDTO defaultWithdrawalTransactionCreateDTO(){
        return TransactionCreateDTO.builder()
                .senderAccountId("c34f11df-cda3-4d75-a74b-4d8c98d6074f")
                .amount(500)
                .build();
    }

    public static TransactionCreateDTO defaultDepositTransactionCreateDTO(){
        return TransactionCreateDTO.builder()
                .receiverAccountId("c34f11df-1234-4d75-a74b-4d8c98d6074f")
                .amount(500)
                .build();
    }
}
