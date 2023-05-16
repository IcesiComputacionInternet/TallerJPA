package co.edu.icesi.tallerjpa.unit.controller;

import co.edu.icesi.tallerjpa.controller.IcesiAccountController;
import co.edu.icesi.tallerjpa.dto.IcesiAccountCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;
import co.edu.icesi.tallerjpa.service.IcesiAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
public class IcesiAccountControllerTest {
    private IcesiAccountController icesiAccountController;
    private IcesiAccountService icesiAccountService;

    @BeforeEach
    private void init(){
        icesiAccountService = mock(IcesiAccountService.class);
        icesiAccountController = new IcesiAccountController(icesiAccountService);
    }

    private IcesiAccountCreateDTO regularIcesiAccountCreateDTO(){
        return IcesiAccountCreateDTO.builder()
                .balance(0)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT)
                .active(true)
                .icesiUserDTO(defaultIcesiUserCreateDTO())
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

    private IcesiRoleCreateDTO defaultIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Manage the system")
                .name("Admin")
                .build();
    }


}
