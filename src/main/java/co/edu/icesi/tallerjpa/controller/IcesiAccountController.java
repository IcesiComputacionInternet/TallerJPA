package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.api.IcesiAccountApi;
import co.edu.icesi.tallerjpa.dto.*;
import co.edu.icesi.tallerjpa.security.IcesiSecurityContext;
import co.edu.icesi.tallerjpa.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class IcesiAccountController implements IcesiAccountApi {

    private final IcesiAccountService icesiAccountService;


    @Override
    public IcesiAccountShowDTO createIcesiAccount(@Valid IcesiAccountCreateDTO icesiAccountCreateDTO) {
        String icesiUserId = IcesiSecurityContext.getCurrentUserId();
        return icesiAccountService.save(icesiAccountCreateDTO, icesiUserId);
    }

    @Override
    public IcesiAccountShowDTO enableAccount(String accountNumber) {
        String icesiUserId = IcesiSecurityContext.getCurrentUserId();
        return icesiAccountService.enableAccount(accountNumber, icesiUserId);
    }

    @Override
    public IcesiAccountShowDTO disableAccount(String accountNumber) {
        String icesiUserId = IcesiSecurityContext.getCurrentUserId();
        return icesiAccountService.disableAccount(accountNumber, icesiUserId);
    }

    @Override
    public TransactionResultDTO withdrawalMoney(TransactionCreateDTO transactionCreateDTO) {
        String icesiUserId = IcesiSecurityContext.getCurrentUserId();
        return icesiAccountService.withdrawalMoney(transactionCreateDTO, icesiUserId);
    }

    @Override
    public TransactionResultDTO depositMoney(TransactionCreateDTO transactionCreateDTO) {
        String icesiUserId = IcesiSecurityContext.getCurrentUserId();
        return icesiAccountService.depositMoney(transactionCreateDTO, icesiUserId);
    }

    @Override
    public TransactionResultDTO transferMoney(TransactionCreateDTO transactionCreateDTO) {
        String icesiUserId = IcesiSecurityContext.getCurrentUserId();
        return icesiAccountService.transferMoney(transactionCreateDTO, icesiUserId);
    }

    @Override
    public IcesiAccountShowDTO getAccountByAccountNumber(String accountId) {
        String icesiUserId = IcesiSecurityContext.getCurrentUserId();
        return icesiAccountService.getAccountByAccountNumber(accountId, icesiUserId);
    }
}
