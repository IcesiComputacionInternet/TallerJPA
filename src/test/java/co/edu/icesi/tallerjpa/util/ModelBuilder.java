package co.edu.icesi.tallerjpa.util;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;

import java.util.UUID;

public class ModelBuilder {
    public static IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name(NameIcesiRole.USER.toString())
                .build();

    }

    public static IcesiRole adminIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name(NameIcesiRole.ADMIN.toString())
                .build();

    }

    public static IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c75bf838-8f38-403d-b64f-cca8b7a181d8"))
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(defaultIcesiRole())
                .icesiAccounts(null)
                .build();
    }

    public static IcesiUser adminIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("7e39d68f-dc03-4634-92d1-37bb4b1865e3"))
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(adminIcesiRole())
                .icesiAccounts(null)
                .build();
    }

    public static IcesiAccount regularIcesiAccountCreateWith1000(){
        return IcesiAccount.builder()
                .balance(1000)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }

    public static IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.fromString("2d032e11-2bec-4983-9c35-3a00d8750809"))
                .accountNumber("012-012345-01")
                .balance(500)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }

    public static IcesiAccount defaultIcesiAccountWith0(){
        return IcesiAccount.builder()
                .accountId(null)
                .accountNumber("012-012345-01")
                .balance(0)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }

}
