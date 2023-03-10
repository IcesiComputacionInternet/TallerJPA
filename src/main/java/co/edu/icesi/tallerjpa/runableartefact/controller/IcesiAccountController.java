package co.edu.icesi.tallerjpa.runableartefact.controller;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IcesiAccountController {

    private final IcesiAccountService icesiAccountService;

    @PostMapping("/account/create")
    public String createAccount(@RequestBody IcesiAccountDTO icesiAccountDTO) {
        return icesiAccountService.saveNewAccount(icesiAccountDTO);
    }

    @PostMapping("/account/activate")
    public String activateAccount(@RequestBody String accountNumber) {
        return icesiAccountService.activateAccount(accountNumber);
    }

    @PostMapping("/account/deactivate")
    public String deactivateAccount(@RequestBody String accountNumber) {
        return icesiAccountService.deactivateAccount(accountNumber);
    }
}
