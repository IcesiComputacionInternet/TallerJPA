package co.com.icesi.demojpa.controller;

import co.com.icesi.demojpa.dto.TransactionOperationDTO;
import co.com.icesi.demojpa.dto.TransactionResultDTO;
import co.com.icesi.demojpa.service.AccountService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/accounts")
public class AccountController {

    private AccountService accountService;

    @PatchMapping("/withdraw")
    public TransactionResultDTO withdraw(@RequestBody TransactionOperationDTO transaction){
        //cambiar método para recibir transactionoperationDTO
        return accountService.withdraw(transaction);
    }

    @PatchMapping("/deposit")
    public TransactionResultDTO deposit(@RequestBody TransactionOperationDTO transaction){
        //cambiar método para recibir transactionoperationDTO
        return accountService.deposit(transaction);
    }

    @PatchMapping("/transfer")
    public TransactionResultDTO transfer(@RequestBody TransactionOperationDTO transaction){
        //cambiar método para recibir transactionoperationDTO
        return accountService.transferMoney(transaction);
    }
}
