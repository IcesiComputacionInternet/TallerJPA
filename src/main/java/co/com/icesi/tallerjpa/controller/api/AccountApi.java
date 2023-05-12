package co.com.icesi.tallerjpa.controller.api;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping(AccountApi.ACCOUNT_BASE_URL)
public interface AccountApi {
    String ACCOUNT_BASE_URL = "/accounts";

    @PostMapping
    ResponseAccountDTO add(@Valid @RequestBody RequestAccountDTO account);

    @PatchMapping("/withdraw")
    TransactionDTO withdraw(@Valid @RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/deposit")
    TransactionDTO deposit(@Valid @RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/transfer")
    TransactionDTO transfer(@Valid @RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/enable/{accountNumber}")
    TransactionDTO enableAccount(@PathVariable String accountNumber);

    @PatchMapping("/disable/{accountNumber}")
    TransactionDTO disableAccount(@PathVariable String accountNumber);

}
