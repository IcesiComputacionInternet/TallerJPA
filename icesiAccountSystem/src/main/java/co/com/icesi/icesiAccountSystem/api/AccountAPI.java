package co.com.icesi.icesiAccountSystem.api;

import co.com.icesi.icesiAccountSystem.dto.RequestAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.TransactionOperationDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(AccountAPI.BASE_ACCOUNT_URL)
public interface AccountAPI {
    String BASE_ACCOUNT_URL="/accounts";
    @GetMapping("/{accountNumber}")
    ResponseAccountDTO getAccount(@PathVariable("accountNumber")  String accountNumber);
    @GetMapping("/getList")
    List<ResponseAccountDTO> getAllAccounts();
    @PostMapping("/create")
    ResponseAccountDTO createAccount(@Valid @RequestBody RequestAccountDTO requestAccountDTO);
    @PatchMapping("/enable/{accountNumber}")
    ResponseAccountDTO enableAccount(@PathVariable("accountNumber") String accountNumber);
    @PatchMapping("/disable/{accountNumber}")
    ResponseAccountDTO disableAccount(@PathVariable("accountNumber")  String accountNumber);
    @PatchMapping("/withdraw")
    TransactionOperationDTO withdrawMoney(@Valid @RequestBody TransactionOperationDTO transaction);
    @PatchMapping("/deposit")
    TransactionOperationDTO depositMoney(@Valid @RequestBody TransactionOperationDTO transaction);
    @PatchMapping("/transfer")
    TransactionOperationDTO transferMoney(@Valid @RequestBody TransactionOperationDTO transaction);
}
