package co.com.icesi.TallerJPA.api;


import co.com.icesi.TallerJPA.dto.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/account")
public interface AccountApi {

    @PostMapping
    IcesiAccountDTOResponse createAccount(@Valid @RequestBody IcesiAccountDTO accountDTO);

    @PatchMapping("/enable/{accountNumber}")
    ActionResultDTO enableAccount(@PathVariable String accountNumber);

    @PatchMapping("/disable/{accountNumber}")
    ActionResultDTO disableAccount(@PathVariable String accountNumber);

    @PatchMapping("/transferMoney")
    TransactionResponseDTO transferMoney(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO);

    @PatchMapping("/withdrawalMoney")
    TransactionResponseDTO withdrawalMoney(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO);

    @PatchMapping("/depositMoney")
    TransactionResponseDTO depositMoney(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO);



}
