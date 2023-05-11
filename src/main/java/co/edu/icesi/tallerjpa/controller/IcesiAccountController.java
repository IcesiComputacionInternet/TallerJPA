package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.api.IcesiAccountApi;
import co.edu.icesi.tallerjpa.dto.*;
import co.edu.icesi.tallerjpa.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class IcesiAccountController implements IcesiAccountApi {

    private final IcesiAccountService icesiAccountService;


    @Override
    public IcesiAccountShowDTO createIcesiAccount(IcesiAccountCreateDTO icesiAccountCreateDTO) {
        return icesiAccountService.save(icesiAccountCreateDTO);
    }

    @Override
    public IcesiAccountShowDTO enableAccount(String accountId) {
        return icesiAccountService.enableAccount(accountId);
    }

    @Override
    public IcesiAccountShowDTO disableAccount(String accountId) {
        return icesiAccountService.disableAccount(accountId);
    }

    @Override
    public TransactionResultDTO withdrawalMoney(TransactionCreateDTO transactionCreateDTO) {
        return icesiAccountService.withdrawalMoney(transactionCreateDTO);
    }

    @Override
    public TransactionResultDTO depositMoney(TransactionCreateDTO transactionCreateDTO) {
        return icesiAccountService.depositMoney(transactionCreateDTO);
    }

    @Override
    public TransactionResultDTO transferMoney(TransactionCreateDTO transactionCreateDTO) {
        return icesiAccountService.transferMoney(transactionCreateDTO);
    }

    @Override
    public IcesiAccountShowDTO getAccountByAccountNumber(String accountId) {
        return icesiAccountService.getAccountByAccountNumber(accountId);
    }
}
