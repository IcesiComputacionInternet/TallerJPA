package co.com.icesi.tallerjpa.api;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.ResponseAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface AccountAPI {
    String ACCOUNT_URL = "/accounts";
    @PostMapping
    ResponseAccountDTO addAccount(@Valid @RequestBody RequestAccountDTO accountDTO);

    @PatchMapping("/enable")
    ResponseAccountDTO enableAcc(@PathVariable String accNum);

    @PatchMapping("/disable")
    ResponseAccountDTO disableAcc(@PathVariable String accNum);

    @PatchMapping("/withdrawal")
    TransactionDTO withdrawal(@Valid @RequestBody TransactionDTO transacDTO);

    @PatchMapping("/deposit")
    TransactionDTO deposit(@Valid @RequestBody TransactionDTO transacDTO);

    @PatchMapping("/transfer")
    TransactionDTO tranfer(@Valid @RequestBody TransactionDTO transacDTO);
}
