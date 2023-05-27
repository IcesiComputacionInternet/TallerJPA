package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiAccountCreateDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiTransactionDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiAccountCreateResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface IcesiAccountAPI {
    String BASE_ACCOUNT_URL = "/accounts";

    @GetMapping("/getAccounts")
    List<IcesiAccountCreateResponseDTO> getAllAccounts();

    @GetMapping("/{accountNumber}")
    IcesiAccountCreateDTO getAccountByNumber(@PathVariable String accountNumber);

    @PostMapping("/create")
    IcesiAccountCreateResponseDTO addAccount(@Valid @RequestBody IcesiAccountCreateDTO accountDto);

    @PatchMapping("/depositOnly")
    IcesiTransactionDTO depositOnlyMoney(@Valid @RequestBody IcesiTransactionDTO transactionDTODeposit);

    @PatchMapping("/withdraw")
    IcesiTransactionDTO withdrawMoney(@Valid @RequestBody IcesiTransactionDTO transactionDTOWithdraw);

    @PatchMapping("/transferMoney")
    IcesiTransactionDTO transferMoney(@Valid @RequestBody IcesiTransactionDTO transactionDTOMoney);

    @PatchMapping("/enableAccount/{accountNumber}")
    String enableAccount(@PathVariable String accountNumber);

    @PatchMapping("/disableAccount/{accountNumber}")
    String disableAccount(@PathVariable String accountNumber);
}
