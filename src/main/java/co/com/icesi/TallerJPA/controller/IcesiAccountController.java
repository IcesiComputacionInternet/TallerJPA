package co.com.icesi.TallerJPA.controller;

import co.com.icesi.TallerJPA.api.AccountApi;
import co.com.icesi.TallerJPA.dto.*;
import co.com.icesi.TallerJPA.service.IcesiAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class IcesiAccountController implements AccountApi {
    private final IcesiAccountService accountService;


    @Override
    public IcesiAccountDTOResponse createAccount(IcesiAccountDTO accountDTO) {
        return accountService.save(accountDTO);
    }

    @Override
    public ActionResultDTO enableAccount(String accountNumber) {
        return accountService.enableAccount(accountNumber);
    }

    @Override
    public ActionResultDTO disableAccount(String accountNumber) {
        return accountService.disableAccount(accountNumber);
    }

    @Override
    public TransactionResponseDTO transferMoney(TransactionRequestDTO transactionRequestDTO) {
        return accountService.transferMoney(transactionRequestDTO);
    }

    @Override
    public TransactionResponseDTO withdrawalMoney(TransactionRequestDTO transactionRequestDTO) {
        return accountService.withdrawalMoney(transactionRequestDTO);
    }

    @Override
    public TransactionResponseDTO depositMoney(TransactionRequestDTO transactionRequestDTO) {
        return accountService.depositMoney(transactionRequestDTO);
    }
}
