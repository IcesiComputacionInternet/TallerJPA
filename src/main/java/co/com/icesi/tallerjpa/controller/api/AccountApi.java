package co.com.icesi.tallerjpa.controller.api;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccountApi {

    String BASE_URL = "/accounts";

    @PostMapping("/add")
    ResponseAccountDTO save(@RequestBody RequestAccountDTO account);

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
