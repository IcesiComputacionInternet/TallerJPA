package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.response.ResponseAccountDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static co.com.icesi.demojpa.api.AccountAPI.BASE_ACCOUNT_URL;

@RequestMapping(value = BASE_ACCOUNT_URL)
public interface AccountAPI {
    String BASE_ACCOUNT_URL ="/accounts";

    @PostMapping("/")
    ResponseAccountDTO createIcesiAccount(@RequestBody AccountCreateDTO accountCreateDTO);

    @PostMapping("/disableAccount/{accountNumber}")
    void disableAccount(@PathVariable String accountNumber);

    @PostMapping("/eneableAccount/{accountNumber}")
    void eneableAccount(String accountNumber);

    @PostMapping("/withdraw/{accountNumber}/{amount}")
    void withdraw(String accountNumber, long amount);

    @PostMapping("/deposit/{accountNumber}/{amount}")
    void deposit(String accountNumber, long amount);

    @PostMapping("/transfer/{accountNumberOrigin}/{accountNumberDestination}/{amount}")
    void transfer(String accountNumberOrigin, String accountNumberDestination, long amount);


}
