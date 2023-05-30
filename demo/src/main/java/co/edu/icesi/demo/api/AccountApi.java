package co.edu.icesi.demo.api;

import co.edu.icesi.demo.dto.AccountsUserDto;
import co.edu.icesi.demo.dto.IcesiAccountDto;
import co.edu.icesi.demo.dto.TransactionOperationDto;
import co.edu.icesi.demo.dto.TransactionResultDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/Accounts")
@CrossOrigin(origins = "*")
public interface AccountApi {

    @PostMapping("/add/account")
    public IcesiAccountDto save(@RequestBody @Valid IcesiAccountDto account);
    @PatchMapping("/withdraw/")
    public TransactionResultDto withdraw(@Valid @RequestBody TransactionOperationDto transaction);

    @PatchMapping("/deposit/")
    public TransactionResultDto deposit(@Valid @RequestBody TransactionOperationDto transaction);

    @PatchMapping("/transfer/")
    public TransactionResultDto transfer(@Valid @RequestBody TransactionOperationDto transaction);

    @PatchMapping("/enableAccount/{accountNumber}")
    public String enableAccount(@PathVariable String accountNumber);

    @PatchMapping("/disableAccount/{accountNumber}")
    public String disableAccount(@PathVariable String accountNumber);


    @GetMapping("/getAccounts")
    public AccountsUserDto getAccountsLoggedUser();
}
