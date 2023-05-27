package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.AccountCreateDTO;
import co.com.icesi.TallerJPA.dto.TransactionOperationDTO;
import co.com.icesi.TallerJPA.dto.response.AccountResponseDTO;
import co.com.icesi.TallerJPA.dto.response.AccountsDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiAccountAPI.BASE_ACCOUNT_URL;

@RequestMapping(value = BASE_ACCOUNT_URL)
public interface IcesiAccountAPI {
        String BASE_ACCOUNT_URL = "/accounts";

        @PostMapping
        AccountResponseDTO createIcesiAccount(@Valid @RequestBody AccountCreateDTO account);
        @PatchMapping("/enable/{accountNumber}")
        String enableAccount(@PathVariable String accountNumber);

        @PatchMapping("/disable/{accountNumber}")
        String disableAccount(@PathVariable String accountNumber);
        @PatchMapping("/withdraw")
        TransactionOperationDTO withdraw(@Valid @RequestBody TransactionOperationDTO transaction);

        @PatchMapping("/deposit")
        TransactionOperationDTO deposit(@Valid @RequestBody TransactionOperationDTO transaction);

        @PatchMapping("/transfer")
        TransactionOperationDTO transfer(@Valid @RequestBody TransactionOperationDTO transaction);

        @GetMapping("/{accountNumber}")
        AccountResponseDTO getAccountByNumber(@PathVariable String accountNumber);

        @GetMapping("/allAccounts")
        List<AccountsDTO> getAllAccounts();

        @GetMapping("/allAccountsByUser/{email}")
        List<AccountsDTO> getAllAccountsFromUser(@PathVariable String email);

}
