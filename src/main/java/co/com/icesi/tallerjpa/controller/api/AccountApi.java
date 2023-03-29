package co.com.icesi.tallerjpa.controller.api;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

import static co.com.icesi.tallerjpa.controller.api.AccountApi.ACCOUNT_BASE_URL;


@RequestMapping(UserApi.USER_BASE_URL)
public interface AccountApi {

    String ACCOUNT_BASE_URL = "/accounts";

    @PostMapping
    ResponseAccountDTO add(@RequestBody RequestAccountDTO account);

    @PatchMapping("/withdraw")
    String withdraw(@RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/deposit")
    String deposit(@RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/transfer")
    String transfer(@RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/enable/{accountNumber}")
    String enableAccount(@PathVariable String accountNumber);

    @PatchMapping("/disable/{accountNumber}")
    String disableAccount(@PathVariable String accountNumber);

}
