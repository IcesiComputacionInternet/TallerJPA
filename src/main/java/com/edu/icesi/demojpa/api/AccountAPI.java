package com.edu.icesi.demojpa.api;

import com.edu.icesi.demojpa.dto.RequestAccountDTO;
import com.edu.icesi.demojpa.dto.RequestTransactionDTO;
import com.edu.icesi.demojpa.dto.ResponseAccountDTO;
import com.edu.icesi.demojpa.dto.ResponseTransactionDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AccountAPI {
    String BASE_ACCOUNT_URL = "/accounts";
    @GetMapping("/{accountNumber}")
    ResponseAccountDTO getAccount(@PathVariable String accountNumber);
    @GetMapping
    List<ResponseAccountDTO> getAllAccounts();
    @PostMapping("/createAccount")
    ResponseAccountDTO createAccount(@RequestBody RequestAccountDTO requestAccountDTO);
    @PatchMapping("/enable")
    ResponseAccountDTO enableAccount(@RequestBody RequestAccountDTO account);
    @PatchMapping("/enable")
    ResponseAccountDTO disableAccount(@RequestBody RequestAccountDTO account);
    @PatchMapping("/withdraw")
    ResponseTransactionDTO withdraw(@RequestBody RequestTransactionDTO transaction);
    @PatchMapping("/deposit")
    ResponseTransactionDTO deposit(@RequestBody RequestTransactionDTO transaction);
    @PatchMapping("/transfer")
    ResponseTransactionDTO transfer(@RequestBody RequestTransactionDTO transaction);
}
