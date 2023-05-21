package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.TransactionOperationDTO;
import co.com.icesi.demojpa.dto.TransactionResultDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping(AccountAPI.BASE_ACCOUNT_URL)
public interface AccountAPI {

    String BASE_ACCOUNT_URL = "/accounts";

    @PostMapping
    AccountCreateDTO add(@Valid @RequestBody AccountCreateDTO account);

    @PostMapping("/withdraw")
    TransactionResultDTO withdraw(@Valid @RequestBody TransactionOperationDTO transactionDTO);

    @PostMapping("/deposit")
    TransactionResultDTO deposit(@Valid @RequestBody TransactionOperationDTO transactionDTO);

    @PostMapping("/transfer")
    TransactionResultDTO transfer(@Valid @RequestBody TransactionOperationDTO transactionDTO);

    @PatchMapping("/enable/{accountNumber}")
    String enableAccount(@PathVariable String accountNumber);

    @PatchMapping("/disable/{accountNumber}")
    String disableAccount(@PathVariable String accountNumber);

}
