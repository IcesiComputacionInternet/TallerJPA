package com.example.TallerJPA.api;

import com.example.TallerJPA.dto.AccountResponseDTO;

public interface AccountAPI {
    String BASE_ACCOUNT_URL = "/accounts";

    public AccountResponseDTO createAccount(AccountResponseDTO accountResponseDTO);

}
