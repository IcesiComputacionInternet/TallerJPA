package co.edu.icesi.demo.api;

import co.edu.icesi.demo.dto.AccountDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface AccountAPI {

    String BASE_ACCOUNT_URL="/accounts";

    @GetMapping("/{accountNumber}")
    AccountDTO getAccount(@PathVariable String accountNumber);

    @GetMapping
    List<AccountDTO> getAllAccounts();

    @PostMapping
    AccountDTO addAccount(@Valid @RequestBody AccountDTO accountDTO);

    @PutMapping("/withdrawal")
    TransactionDTO withdrawalMoney(@Valid @RequestBody TransactionDTO transactionDTO);

    @PutMapping("/deposit")
    TransactionDTO depositMoney(@Valid @RequestBody TransactionDTO transactionDTO);

    @PutMapping("/transfer")
    TransactionDTO transferMoney(@Valid @RequestBody TransactionDTO transactionDTO);

    @PutMapping("/{accountNumber}/enable")
    AccountDTO enableAccount(@PathVariable String accountNumber);

    @PutMapping("/{accountNumber}/disable")
    AccountDTO disableAccount(@PathVariable String accountNumber);

}
