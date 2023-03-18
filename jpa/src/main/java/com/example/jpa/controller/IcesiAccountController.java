package com.example.jpa.controller;

import com.example.jpa.dto.AccountRequestDTO;
import com.example.jpa.dto.AccountResponseDTO;
import com.example.jpa.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/accounts")
public class IcesiAccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponseDTO createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        return accountService.save(accountRequestDTO);
    }

    @GetMapping
    public List<AccountResponseDTO> getAccount() {
        return accountService.getAccounts();
    }
}
