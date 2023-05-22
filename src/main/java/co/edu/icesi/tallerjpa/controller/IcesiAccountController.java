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

    private final IcesiAccountService accountService;

    @PostMapping("/account/create")
    public IcesiAccount createAccount(@RequestBody IcesiAccountDTO accountDTO) throws UserNotFoundException, DuplicateDataException, NegativeBalanceException, MissingParameterException {
        return accountService.createAccount(accountDTO);
    }

    @PostMapping("/account/activate")
    public String activateAccount(@RequestBody String accountNumber) throws AccountBalanceNotZeroException, AccountNotFoundException {
        return accountService.disableAccount(accountNumber);
    }

    @PostMapping("/account/deactivate")
    public String deactivateAccount(@RequestBody String accountNumber) {
        return accountService.enableAccount(accountNumber);
    }

    @PostMapping("/account/withdrawal")
    public String withdrawMoney(@RequestBody String accountNumber, @RequestBody Long amount) {
        return accountService.withdrawMoney(accountNumber, amount);
    }

    @PostMapping("/account/deposit")
    public String depositMoney(@RequestBody String accountNumber, @RequestBody Long amount) {
        return accountService.depositMoney(accountNumber, amount);
    }
}

