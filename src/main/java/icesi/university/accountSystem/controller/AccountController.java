package icesi.university.accountSystem.controller;
import icesi.university.accountSystem.api.AccountAPI;
import icesi.university.accountSystem.dto.RequestAccountDTO;
import icesi.university.accountSystem.dto.ResponseAccountDTO;
import icesi.university.accountSystem.dto.TransactionOperationDTO;
import icesi.university.accountSystem.dto.TransactionResultDTO;
import icesi.university.accountSystem.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static icesi.university.accountSystem.api.AccountAPI.BASE_ACCOUNT_URL;
@RestController
@AllArgsConstructor
public class AccountController implements AccountAPI {

    private final AccountService accountService;


    @PostMapping(BASE_ACCOUNT_URL+"/add")
    @Override
    public ResponseAccountDTO add(RequestAccountDTO account) {
        return accountService.save(account);
    }


    @PatchMapping(BASE_ACCOUNT_URL+"/withdraw")
    @Override
    public TransactionResultDTO withdraw(TransactionOperationDTO transactionDTO) {
        return accountService.withdrawal(transactionDTO);
    }
    @PatchMapping(BASE_ACCOUNT_URL+"/deposit")
    @Override
    public TransactionResultDTO deposit(TransactionOperationDTO transactionDTO) {
        return accountService.deposit(transactionDTO);
    }
    @PatchMapping(BASE_ACCOUNT_URL+"/transfer")
    @Override
    public TransactionResultDTO transfer(TransactionOperationDTO transactionDTO) {
        return accountService.transfer(transactionDTO);
    }
    @PatchMapping(BASE_ACCOUNT_URL+"/activate/{accountNumber}")
    @Override
    public String activateAccount(String accountNumber) {

        return accountService.activateAccount(accountNumber);
    }
    @PatchMapping(BASE_ACCOUNT_URL+"/deactivate/{accountNumber}")
    @Override
    public String deactivateAccount(String accountNumber) {
        return accountService.deactivateAccount(accountNumber);
    }
}
