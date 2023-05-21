package com.edu.icesi.TallerJPA.api;

import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.dto.TransactionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface AccountAPI {

    String BASE_ACCOUNT_URL = "/accounts";

    @GetMapping("/{accountNumber}/")
    AccountCreateDTO getAccount(@PathVariable String accountNumber);

    List<AccountCreateDTO> getAllUsers();

    AccountCreateDTO addAccount(@Valid @RequestBody AccountCreateDTO accountCreateDTO);

    @PatchMapping("/withdraw/")
    TransactionDTO withdraw(@Valid @RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/deposit/")
    TransactionDTO deposit(@Valid @RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/transfer/")
    TransactionDTO transfer(@Valid @RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/enable/{accountNumber}")
    AccountCreateDTO enable(@PathVariable String accountNumber);

    @PatchMapping("/disable/{accountNumber}")
    AccountCreateDTO disable(@PathVariable String accountNumber);
}
