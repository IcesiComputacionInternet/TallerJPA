package co.edu.icesi.demo.api;

import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AccountAPI {

    String BASE_ACCOUNT_URL="/accounts";

    @GetMapping("/{accountNumber}")
    AccountCreateDTO getAccount(@PathVariable String accountNumber);

    @GetMapping
    List<AccountCreateDTO> getAllAccounts();

    @PostMapping
    AccountCreateDTO addAccount(AccountCreateDTO accountCreateDTO);

    @PutMapping("/withdrawal")
    TransactionDTO withdrawalMoney(TransactionDTO transactionDTO);

    @PutMapping("/deposit")
    TransactionDTO depositMoney(TransactionDTO transactionDTO);

    @PutMapping("/transfer")
    TransactionDTO transferMoney(TransactionDTO transactionDTO);

    @PutMapping("/enable/{accountNumber}")
    AccountCreateDTO enableAccount(@PathVariable String accountNumber);

    @PutMapping("/disable/{accountNumber}")
    AccountCreateDTO disableAccount(@PathVariable String accountNumber);

}
