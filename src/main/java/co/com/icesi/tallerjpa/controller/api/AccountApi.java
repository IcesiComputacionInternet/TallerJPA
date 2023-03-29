package co.com.icesi.tallerjpa.controller.api;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

@RequestMapping(AccountApi.ACCOUNT_BASE_URL)
public interface AccountApi {
    String ACCOUNT_BASE_URL = "/accounts";

    @PostMapping
    ResponseAccountDTO add(@RequestBody RequestAccountDTO account);

    @PatchMapping("/withdraw")
    TransactionDTO withdraw(@RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/deposit")
    TransactionDTO deposit(@RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/transfer")
    TransactionDTO transfer(@RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/enable/{accountNumber}")
    TransactionDTO enableAccount(@PathVariable String accountNumber);

    @PatchMapping("/disable/{accountNumber}")
    TransactionDTO disableAccount(@PathVariable String accountNumber);

}
