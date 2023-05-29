package co.com.icesi.demojpa.api;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.TransactionOperationDTO;
import co.com.icesi.demojpa.dto.TransactionResultDTO;
import co.com.icesi.demojpa.model.IcesiAccount;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(AccountAPI.BASE_ACCOUNT_URL)
public interface AccountAPI {

    String BASE_ACCOUNT_URL = "/accounts";

    @PostMapping
    AccountCreateDTO add(@Valid @RequestBody AccountCreateDTO account);

    @GetMapping("/all")
    List<AccountCreateDTO> findAll();

    @PostMapping("/withdraw")
    TransactionResultDTO withdraw(@Valid @RequestBody TransactionOperationDTO transactionDTO);

    @PostMapping("/deposit")
    TransactionResultDTO deposit(@Valid @RequestBody TransactionOperationDTO transactionDTO);

    @PostMapping("/transfer")
    TransactionResultDTO transfer(@Valid @RequestBody TransactionOperationDTO transactionDTO);

    @PatchMapping("/enable/{accountNumber}")
    AccountCreateDTO enableAccount(@PathVariable String accountNumber);

    @PatchMapping("/disable/{accountNumber}")
    AccountCreateDTO disableAccount(@PathVariable String accountNumber);

}
