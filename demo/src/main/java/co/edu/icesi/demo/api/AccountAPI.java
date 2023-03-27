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
    AccountCreateDTO addAccount(@RequestBody AccountCreateDTO accountCreateDTO);

    @PutMapping("/withdrawal")
    TransactionDTO withdrawalMoney(@RequestBody TransactionDTO transactionDTO);

    @PutMapping("/deposit")
    TransactionDTO depositMoney(@RequestBody TransactionDTO transactionDTO);

    @PutMapping("/transfer")
    TransactionDTO transferMoney(@RequestBody TransactionDTO transactionDTO);

    @PutMapping("/{accountNumber}/enable")
    AccountCreateDTO enableAccount(@PathVariable String accountNumber);

    @PutMapping("/{accountNumber}/disable")
    AccountCreateDTO disableAccount(@PathVariable String accountNumber);

}
