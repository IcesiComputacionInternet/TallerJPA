package co.edu.icesi.tallerjpa.api;

import co.edu.icesi.tallerjpa.dto.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RequestMapping("/icesi_accounts")
public interface IcesiAccountApi {
    public static final String ROOT_PATH = "/icesi_accounts";
    @PostMapping
    public IcesiAccountShowDTO createIcesiAccount(@Valid @RequestBody IcesiAccountCreateDTO icesiAccountCreateDTO);

    @PatchMapping("enable/{accountNumber}")
    public IcesiAccountShowDTO enableAccount(@PathVariable("accountNumber") String accountNumber);

    @PatchMapping("disable/{accountNumber}")
    public IcesiAccountShowDTO disableAccount(@PathVariable("accountNumber") String accountNumber);

    @PatchMapping("withdrawal_money")
    public TransactionWithOneAccountCreateDTO withdrawalMoney(@Valid @RequestBody TransactionWithOneAccountCreateDTO transactionCreateDTO);

    @PatchMapping("deposit_money")
    public TransactionWithOneAccountCreateDTO depositMoney(@Valid @RequestBody TransactionWithOneAccountCreateDTO transactionCreateDTO);

    @PatchMapping("transfer_money")
    public TransactionResultDTO transferMoney(@Valid @RequestBody TransactionCreateDTO transactionCreateDTO);

    @GetMapping("id/{accountNumber}")
    public IcesiAccountShowDTO getAccountByAccountNumber(@PathVariable("accountNumber") String accountNumber);

}
