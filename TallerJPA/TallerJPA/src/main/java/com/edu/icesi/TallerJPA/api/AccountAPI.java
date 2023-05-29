package com.edu.icesi.TallerJPA.api;

import com.edu.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.edu.icesi.TallerJPA.dto.IcesiAccountGetDTO;
import com.edu.icesi.TallerJPA.dto.IcesiTransactionDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface AccountAPI {

    String BASE_ACCOUNT_URL = "/accounts";

    @GetMapping("/{accountNumber}/")
    IcesiAccountDTO getAccount(@PathVariable String accountNumber);

    List<IcesiAccountDTO> getAllUsers();

    IcesiAccountDTO addAccount(@Valid @RequestBody IcesiAccountDTO accountCreateDTO);

    @PatchMapping("/withdraw/")
    IcesiTransactionDTO withdraw(@Valid @RequestBody IcesiTransactionDTO icesiTransactionDTO);

    @PatchMapping("/deposit/")
    IcesiTransactionDTO deposit(@Valid @RequestBody IcesiTransactionDTO icesiTransactionDTO);

    @PatchMapping("/transfer/")
    IcesiTransactionDTO transfer(@Valid @RequestBody IcesiTransactionDTO icesiTransactionDTO);

    @PatchMapping("/enable/{accountNumber}")
    IcesiAccountDTO enable(@PathVariable String accountNumber);

    @PatchMapping("/disable/{accountNumber}")
    IcesiAccountDTO disable(@PathVariable String accountNumber);

    @GetMapping("/{idUser}")
    List<IcesiAccountGetDTO> findByAccounts(@PathVariable UUID idUser);
}
