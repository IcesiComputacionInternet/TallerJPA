package co.edu.icesi.tallerjpa.controller;

import co.edu.icesi.tallerjpa.api.IcesiAccountApi;
import co.edu.icesi.tallerjpa.dto.*;
import co.edu.icesi.tallerjpa.security.IcesiSecurityContext;
import co.edu.icesi.tallerjpa.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static co.edu.icesi.tallerjpa.security.IcesiSecurityContext.getCurrentUserId;

@AllArgsConstructor
@RestController
public class IcesiAccountController implements IcesiAccountApi {

    private final IcesiAccountService icesiAccountService;


    @Override
    public IcesiAccountShowDTO createIcesiAccount(@Valid IcesiAccountCreateDTO icesiAccountCreateDTO) {
        return icesiAccountService.save(icesiAccountCreateDTO);
    }

    @Override
    public IcesiAccountShowDTO enableAccount(String accountId) {
        String icesiUserId = IcesiSecurityContext.getCurrentUserId();
        var temp = SecurityContextHolder.getContext();
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

    @GetMapping("/admin/pathTest/")
    public void pathTest(){
        var temp = SecurityContextHolder.getContext();
        System.out.println("I'm admin");
    }

    @GetMapping("/user/pathTest/")
    public void pathTest2(){
        System.out.println("I'm user");
    }
}
