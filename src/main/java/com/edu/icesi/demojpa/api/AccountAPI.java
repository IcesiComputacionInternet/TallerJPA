package com.edu.icesi.demojpa.api;

import com.edu.icesi.demojpa.dto.request.RequestAccountDTO;
import com.edu.icesi.demojpa.dto.request.RequestTransactionDTO;
import com.edu.icesi.demojpa.dto.response.ResponseAccountDTO;
import com.edu.icesi.demojpa.dto.response.ResponseTransactionDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface AccountAPI {
    String BASE_ACCOUNT_URL = "/accounts";
    @GetMapping("/{accountNumber}")
    ResponseAccountDTO getAccount(@PathVariable String accountNumber);
    @GetMapping("/getAccounts")
    List<ResponseAccountDTO> getAllAccounts();
    @PostMapping("/create")
    ResponseAccountDTO createAccount(@Valid @RequestBody RequestAccountDTO requestAccountDTO);
    @PatchMapping("/enable")
    ResponseAccountDTO enableAccount(@Valid @RequestBody RequestAccountDTO account);
    @PatchMapping("/disable")
    ResponseAccountDTO disableAccount(@Valid @RequestBody RequestAccountDTO account);
    @PatchMapping("/withdraw")
    ResponseTransactionDTO withdraw(@Valid @RequestBody RequestTransactionDTO transaction);
    @PatchMapping("/deposit")
    ResponseTransactionDTO deposit(@Valid @RequestBody RequestTransactionDTO transaction);
    @PatchMapping("/transfer")
    ResponseTransactionDTO transfer(@Valid @RequestBody RequestTransactionDTO transaction);
}
