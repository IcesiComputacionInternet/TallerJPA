package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.AccountCreateDTO;
import co.com.icesi.TallerJPA.dto.TransactionOperationDTO;
import co.com.icesi.TallerJPA.dto.response.AccountResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static co.com.icesi.TallerJPA.api.IcesiAccountAPI.BASE_ACCOUNT_URL;

@RequestMapping(value = BASE_ACCOUNT_URL)
public interface IcesiAccountAPI {
        String BASE_ACCOUNT_URL = "/accounts";

        @GetMapping("/{accountNumber}")
        AccountResponseDTO getAccountByNumber(@PathVariable String accountNumber);

        @GetMapping("allAccounts")
        List<AccountResponseDTO> getAllAccounts();

        @PostMapping("/create")
        AccountResponseDTO createIcesiAccount(@RequestBody AccountCreateDTO account);
        @PatchMapping("/enable/{accountNumber}")
        String enableAccount(@PathVariable String accountNumber);

        @PatchMapping("/disable/{accountNumber}")
        String disableAccount(@PathVariable String accountNumber);
        @PatchMapping("withdraw")
        TransactionOperationDTO withdraw(@RequestBody TransactionOperationDTO transaction);

        @PatchMapping("deposit")
        TransactionOperationDTO deposit(@RequestBody TransactionOperationDTO transaction);

        @PatchMapping
        TransactionOperationDTO transfer(@RequestBody TransactionOperationDTO transaction);

}
