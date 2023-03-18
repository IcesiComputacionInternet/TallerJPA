package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.dto.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.exception.*;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@AllArgsConstructor
public class IcesiAccountController {

    private final IcesiAccountService icesiAccountService;

    @PostMapping("/account/create")
    public IcesiAccount createAccount(@RequestBody IcesiAccountDTO icesiAccountDTO) throws UserNotFoundException, DuplicateDataException, NegativeBalanceException, MissingParameterException {
        return icesiAccountService.createAccount(icesiAccountDTO);
    }

    @PostMapping("/account/activate")
    public String activateAccount(@RequestBody String accountNumber) throws AccountBalanceNotZeroException, AccountNotFoundException {
        return icesiAccountService.disableAccount(accountNumber);
    }

    @PostMapping("/account/deactivate")
    public String enableAccount(@RequestBody String accountNumber) {
        return icesiAccountService.enableAccount(accountNumber);
    }

    @PostMapping("/account/withdrawal")
    public String withdrawMoney(@RequestBody String accountNumber, Long amount) {
        return icesiAccountService.withdrawMoney(accountNumber, amount);
    }

    @PostMapping("/account/deposit")
    public String deposit(@RequestBody String accountNumber, Long amount) {
        return icesiAccountService.depositMoney(accountNumber, amount);
    }


}
