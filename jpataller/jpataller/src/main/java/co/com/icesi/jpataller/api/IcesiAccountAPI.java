package co.com.icesi.jpataller.api;

import co.com.icesi.jpataller.dto.AccountCreateDTO;
import co.com.icesi.jpataller.dto.TransactionDTO;
import co.com.icesi.jpataller.dto.response.ResponseAccountDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static co.com.icesi.jpataller.api.IcesiAccountAPI.BASE_ACCOUNT_URL;

@RequestMapping(value = BASE_ACCOUNT_URL)
public interface IcesiAccountAPI {
    String BASE_ACCOUNT_URL ="/accounts";

    @PostMapping("/")
    ResponseAccountDTO createIcesiAccount(@RequestBody AccountCreateDTO accountCreateDTO);

    @PostMapping("/disableAccount/{accountNumber}")
    void disableAccount(@PathVariable String accountNumber);

    @PostMapping("/enableAccount/{accountNumber}")
    void eneableAccount(String accountNumber);

    @PostMapping("/withdraw/{accountNumber}/{amount}")
    void withdraw(String accountNumber, long amount);

    @PostMapping("/deposit/{accountNumber}/{amount}")
    void deposit(String accountNumber, long amount);

    @PostMapping("/transfer/")
    TransactionDTO transfer( @RequestBody TransactionDTO transactionDTO);


}