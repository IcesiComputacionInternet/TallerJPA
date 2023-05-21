package co.com.icesi.TallerJpa.controller.api;

import co.com.icesi.TallerJpa.dto.IcesiAccountRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiAccountResponseDTO;
import co.com.icesi.TallerJpa.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@RequestMapping(value =IcesiAccountApi.ACCOUNT_BASE_URL)
public interface IcesiAccountApi {

    String ACCOUNT_BASE_URL = "/api/account";

    @PostMapping
    IcesiAccountResponseDTO addIcesiAccount(@Valid @RequestBody IcesiAccountRequestDTO icesiAccountRequestDTO);

    @GetMapping
    List<IcesiAccountResponseDTO> getAllAccounts();

    @GetMapping("/{accountNumber}")
    IcesiAccountResponseDTO getIcesiAccount(@PathVariable String accountNumber);

    @GetMapping("/user/{userId}/accounts")
    List<IcesiAccountResponseDTO> getAllAccountsByUserId(@PathVariable String userId);

    @GetMapping("/user/{email}/accounts")
    List<IcesiAccountResponseDTO> getAllAccountsByEmail(@PathVariable @Valid @Email String email);

    @PatchMapping("/enable/{accountNumber}")
    TransactionDTO enableAccount(@PathVariable String accountNumber);

    @PatchMapping("/disable/{accoutNumber}")
    TransactionDTO disableAccount(@PathVariable String accountNumber);

    @PatchMapping("/withdraw")
    TransactionDTO withdraw(@Valid @RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/deposit")
    TransactionDTO deposit(@Valid @RequestBody TransactionDTO transactionDTO);

    @PatchMapping("/transfer")
    TransactionDTO transfer(@Valid @RequestBody TransactionDTO transactionDTO);
}
