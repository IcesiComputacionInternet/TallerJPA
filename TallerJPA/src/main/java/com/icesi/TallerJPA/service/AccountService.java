package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.IcesiAccountResponseDTO;
import com.icesi.TallerJPA.mapper.AccountMapper;
import com.icesi.TallerJPA.model.IcesiAccount;
import com.icesi.TallerJPA.repository.AccountRepository;
import com.icesi.TallerJPA.repository.UserRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final UserRespository userRespository;

    public IcesiAccountResponseDTO save(IcesiAccountDTO icesiAccountDTO) {
        IcesiAccount icesiAccount = accountMapper.fromIcesiAccountDTO(icesiAccountDTO);
        icesiAccount.setAccountId(UUID.randomUUID());
        icesiAccount.setAccountNumber("12345");
        icesiAccount.setIcesiUser(userRespository.findIcesiUserByEmail(icesiAccountDTO.getEmailUser())
                .orElseThrow(()-> new RuntimeException("User not found")));
        return accountMapper.toResponse(icesiAccount);
    }
}
