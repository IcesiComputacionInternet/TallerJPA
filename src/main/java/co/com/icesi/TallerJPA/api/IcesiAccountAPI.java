package co.com.icesi.TallerJPA.api;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiAccountCreateDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiTransactionDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiAccountCreateResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IcesiAccountAPI {
    String BASE_ACCOUNT_URL = "/accounts";

    @GetMapping
    List<IcesiAccountCreateResponseDTO> getAllAccounts();

    @GetMapping("/{accountNumber}")
    IcesiAccountCreateDTO getAccountByNumber(@PathVariable String accountNumber);

    @PostMapping
    IcesiAccountCreateResponseDTO addAccount(@RequestBody IcesiAccountCreateDTO accountDto);

    @PutMapping("/depositOnly")
    IcesiTransactionDTO depositOnlyMoney(@RequestBody IcesiTransactionDTO transactionDTODeposit);

    @PutMapping("/withdraw")
    IcesiTransactionDTO withdrawMoney(@RequestBody IcesiTransactionDTO transactionDTOWithdraw);

    @PutMapping("/transferMoney")
    IcesiTransactionDTO transferMoney(@RequestBody IcesiTransactionDTO transactionDTOMoney);

    @PutMapping("/enableAccount")
    String enableAccount(@PathVariable String accountNumber);

    @PutMapping("/disableAccount")
    String disableAccount(@PathVariable String accountNumber);
}
